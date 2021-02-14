package com.ironhack.midtermProject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermProject.classes.Address;
import com.ironhack.midtermProject.classes.Money;
import com.ironhack.midtermProject.controller.dto.MoneyDTO;
import com.ironhack.midtermProject.controller.dto.TransferDTO;
import com.ironhack.midtermProject.enums.State;
import com.ironhack.midtermProject.enums.Status;
import com.ironhack.midtermProject.enums.UserRole;
import com.ironhack.midtermProject.model.*;
import com.ironhack.midtermProject.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private CheckingRepository checkingRepository;
    @Autowired
    private StudentCheckingRepository studentCheckingRepository;
    @Autowired
    private SavingsRepository savingsRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        Address address1 = new Address("C/ México 22 1ºD", 28823, "Coslada", State.MADRID);
        Address address2 = new Address("C/ Forja 4",25200 , "Chinchilla", State.ALBACETE);
        Address address3 = new Address("Av/ Llibertat 46 2ºD",36900 , "San Vicente del Raspeig", State.ALICANTE);
        Address address4 = new Address("C/ Escritores 12 6º1", 47009, "Valladolid", State.VALLADOLID);
        AccountHolder owner1 = new AccountHolder("Juan Antonio Fernández", "juan_an_fe69", "patata", LocalDate.of(1969,7,21), address1);
        AccountHolder owner2 = new AccountHolder("Perico Delgado", "pery", "bici", LocalDate.of(2001,4,1), address2);
        AccountHolder owner3 = new AccountHolder("Alfono Navalón", "alfon", "malacara", LocalDate.of(1969,1,15), address3);
        AccountHolder owner4 = new AccountHolder("Pedro González", "pedrito", "pucela", LocalDate.of(1969,10,8), address4);
        accountHolderRepository.saveAll(List.of(owner1 ,owner2, owner3, owner4));
        roleRepository.saveAll(List.of(new Role(UserRole.ACCOUNT_HOLDER, owner1), new Role(UserRole.ACCOUNT_HOLDER, owner2), new Role(UserRole.ACCOUNT_HOLDER, owner3), new Role(UserRole.ACCOUNT_HOLDER, owner4)));

        Admin admin1 = new Admin("Carlos Fernández", "cafla", "risoto");
        adminRepository.save(admin1);
        roleRepository.save(new Role(UserRole.ADMIN, admin1));

        ThirdParty thirdParty1 = new ThirdParty("Tercera Fiesta", "siberiano");
        thirdPartyRepository.save(thirdParty1);

        Checking checking = new Checking(new Money(new BigDecimal("15762")), owner1, "potatomix");
            checking.setCreationDate(LocalDate.now().minusMonths(3));
            checking.setMaintenanceUpdate(checking.getCreationDate());
        StudentChecking studentChecking = new StudentChecking(new Money(new BigDecimal("2512.43")), owner2, "cleta");
        Savings savings = new Savings(new Money(new BigDecimal("900")), owner3, new BigDecimal("0.1"),"caragorrino");
            savings.setBelowMinimumBalance(true);
            savings.setCreationDate(LocalDate.now().minusYears(2));
            savings.setInterestUpdate(savings.getCreationDate());
        CreditCard creditCard = new CreditCard(new Money(new BigDecimal("1500")), owner4, new BigDecimal("0.1"));
            creditCard.setCreationDate(LocalDate.now().minusMonths(14));
            creditCard.setInterestUpdate(creditCard.getCreationDate());
        checkingRepository.save(checking);
        studentCheckingRepository.save(studentChecking);
        savingsRepository.save(savings);
        creditCardRepository.save(creditCard);
    }

    @AfterEach
    void tearDown() {
        transferRepository.deleteAll();
        checkingRepository.deleteAll();
        studentCheckingRepository.deleteAll();
        savingsRepository.deleteAll();
        creditCardRepository.deleteAll();
        accountRepository.deleteAll();
        accountHolderRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        adminRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

    // CHECK BALANCE TESTS:

    @Test
    void checkBalance_validIdAndValidOwner_rightBalance() throws Exception {
        List<Account> accounts = accountRepository.findAll(); // To avoid auto_increment problems.
        MvcResult result = mockMvc.perform(get("/check-balance/"+accounts.get(1).getId())
                .with(user("pery")
                        .password("bici")
                        .roles("ACCOUNT_HOLDER")))
                        .andExpect(status().isOk())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("2512.43"));
    }
    @Test
    void checkBalance_validIdAndAdminLogged_rightBalance() throws Exception {
        List<Account> accounts = accountRepository.findAll(); // To avoid auto_increment problems.
        MvcResult result = mockMvc.perform(get("/check-balance/"+accounts.get(1).getId())
                .with(user("cafla")
                        .password("risoto")
                        .roles("ADMIN")))
                        .andExpect(status().isOk())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("2512.43"));
    }
    @Test
    void checkBalance_validIdButNoUserLogged_exception() throws Exception {
        List<Account> accounts = accountRepository.findAll(); // To avoid auto_increment problems.
        MvcResult result = mockMvc.perform(get("/check-balance/"+accounts.get(0).getId()))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
    @Test
    void checkBalance_invalidId_exception() throws Exception {
        MvcResult result = mockMvc.perform(get("/check-balance/0")
                .with(user("cafla")
                        .password("risoto")
                        .roles("ADMIN")))
                .andExpect(status().isNotFound())
                .andReturn();
    }
    @Test
    void checkBalance_SavingsAccountCheckedTwoYearsAgo_balanceWithInterestApplied() throws Exception {
        List<Account> accounts = accountRepository.findAll(); // To avoid auto_increment problems.
        MvcResult result = mockMvc.perform(get("/check-balance/"+accounts.get(2).getId())
                .with(user("cafla")
                        .password("risoto")
                        .roles("ADMIN")))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("1089")); // 900 * 1.1 * 1.1 (specified non default 0.1 Interest Rate)
    }
    @Test
    void checkBalance_CreditCardAccountChecked14MonthsAgo_balanceWithInterestApplied() throws Exception {
        List<Account> accounts = accountRepository.findAll(); // To avoid auto_increment problems.
        MvcResult result = mockMvc.perform(get("/check-balance/"+accounts.get(3).getId())
                .with(user("cafla")
                        .password("risoto")
                        .roles("ADMIN")))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("1684")); // 1500 * 1.1 * 1.1 * 1.1 .... fourteen times (specified non default 0.1 Interest Rate)
    }
    @Test
    void checkBalance_CheckingAccountChecked3MonthsAgo_balanceWithMaintenanceFeeApplied() throws Exception {
        List<Account> accounts = accountRepository.findAll(); // To avoid auto_increment problems.
        MvcResult result = mockMvc.perform(get("/check-balance/"+accounts.get(0).getId())
                .with(user("cafla")
                        .password("risoto")
                        .roles("ADMIN")))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("15726")); // 15762 - 12 - 12 - 12 (Monthly maintenance fee)
    }


    // MODIFY BALANCE TESTS:

    @Test
    void modifyBalance_ValidMoneyDTOWithAdminLogged_modifiedBalance() throws Exception {
        List<Account> accounts = accountRepository.findAll(); // To avoid auto_increment problems.
        MoneyDTO newBalanceDTO = new MoneyDTO(new BigDecimal("2000"));
        String body = objectMapper.writeValueAsString(newBalanceDTO);
        MvcResult result = mockMvc.perform(
                patch("/admin/modify-balance/"+accounts.get(0).getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("cafla")
                                .password("risoto")
                                .roles("ADMIN"))
        ).andExpect(status().isNoContent()).andReturn();

        Optional<Account> modifiedAccount = accountRepository.findById(accounts.get(0).getId());
        assertEquals(new BigDecimal("2000.00"), modifiedAccount.get().getBalance().getAmount());
    }
    @Test
    void modifyBalance_WrongMoneyDTOWithAdminLogged_exception() throws Exception {
        List<Account> accounts = accountRepository.findAll(); // To avoid auto_increment problems.
        MoneyDTO newBalanceDTO = new MoneyDTO(new BigDecimal("-2000"));
        String body = objectMapper.writeValueAsString(newBalanceDTO);
        MvcResult result = mockMvc.perform(
                patch("/admin/modify-balance/"+accounts.get(0).getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("cafla")
                                .password("risoto")
                                .roles("ADMIN"))
        ).andExpect(status().isBadRequest()).andReturn();
        Optional<Account> modifiedAccount = accountRepository.findById(accounts.get(0).getId());
        assertNotEquals(new BigDecimal("2000.00"), modifiedAccount.get().getBalance().getAmount());
    }
    @Test
    void modifyBalance_ValidMoneyDTOButNoAdminLogged_exception() throws Exception {
        List<Account> accounts = accountRepository.findAll(); // To avoid auto_increment problems.
        MoneyDTO newBalanceDTO = new MoneyDTO(new BigDecimal("2000"));
        String body = objectMapper.writeValueAsString(newBalanceDTO);
        MvcResult result = mockMvc.perform(
                patch("/admin/modify-balance/"+accounts.get(0).getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("cafla")
                                .password("risoto")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isForbidden()).andReturn();
    }
    @Test
    void modifyBalance_ValidMoneyDTOButNoUserLogged_exception() throws Exception {
        List<Account> accounts = accountRepository.findAll(); // To avoid auto_increment problems.
        MoneyDTO newBalanceDTO = new MoneyDTO(new BigDecimal("2000"));
        String body = objectMapper.writeValueAsString(newBalanceDTO);
        MvcResult result = mockMvc.perform(
                patch("/admin/modify-balance/"+accounts.get(0).getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized()).andReturn();
    }


    // TRANSFERS BETWEEN ACCOUNT HOLDERS TESTS:

    @Test
    void transfer_ValidAccounts_transfer() throws Exception {
        List<Account> accountList = accountRepository.findAll();
        Account originAccount = accountList.get(0);
        Account destinationAccount = accountList.get(1);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("juan_an_fe69")
                                .password("patata")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isCreated()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(1, transferList.size());
        assertEquals(new BigDecimal("200.00"), transferList.get(0).getAmount().getAmount());
        originAccount = accountRepository.findAll().get(0);
        assertEquals(new BigDecimal("15562.00"), originAccount.getBalance().getAmount());
    }
    @Test
    void transfer_AccountDoesntExist_exception() throws Exception {
        List<Account> accountList = accountRepository.findAll();
        Account destinationAccount = accountList.get(1);
        TransferDTO transferDTO = new TransferDTO(0L, destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("juan_an_fe69")
                                .password("patata")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isNotFound()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transfer_DestinationNameIsNotDestinationOwner_exception() throws Exception {
        List<Account> accountList = accountRepository.findAll();
        Account originAccount = accountList.get(0);
        Account destinationAccount = accountList.get(1);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), "Ángel Serrano", "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("juan_an_fe69")
                                .password("patata")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isBadRequest()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transfer_NotEnoughFunds_exception() throws Exception {
        List<Account> accountList = accountRepository.findAll();
        Account originAccount = accountList.get(0);
        Account destinationAccount = accountList.get(1);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("20000"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("juan_an_fe69")
                                .password("patata")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isBadRequest()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transfer_NegativeToCreditLimit_newTransfer() throws Exception {     // A Credit card balance can be negative from 0 to "Negative Credit Limit".
        List<Account> accountList = accountRepository.findAll();
        Account originAccount = accountList.get(3);
        Account destinationAccount = accountList.get(1);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("1550"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("pedrito")
                                .password("pucela")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("\"amount\":-50.00")); // A Credit card balance can be negative from 0 to "Negative Credit Limit".

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(1, transferList.size());
        originAccount = accountRepository.findAll().get(3);
        assertEquals(new BigDecimal("-50.00"), originAccount.getBalance().getAmount());
    }
    @Test
    void transfer_NotEnoughFundsBeyondCreditLimit_exception() throws Exception {     // A Credit card balance can't be negative beyond Credit Limit.
        List<Account> accountList = accountRepository.findAll();
        Account originAccount = accountList.get(3);
        Account destinationAccount = accountList.get(1);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("2000"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("pedrito")
                                .password("pucela")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isBadRequest()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transfer_NoLoggedUser_exception() throws Exception {
        List<Account> accountList = accountRepository.findAll();
        Account originAccount = accountList.get(0);
        Account destinationAccount = accountList.get(1);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transfer_LoggedUserIsNotOwner_exception() throws Exception {
        List<Account> accountList = accountRepository.findAll();
        Account originAccount = accountList.get(0);
        Account destinationAccount = accountList.get(1);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("pery")
                                .password("bici")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isForbidden()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transfer_FraudDetected150percent_exception() throws Exception {
        List<Account> accountList = accountRepository.findAll();
        Account originAccount = accountList.get(0);
        Account destinationAccount = accountList.get(2);

            // Previous transfers to check fraud
            Transfer transfer1 = new Transfer(originAccount, destinationAccount, destinationAccount.getPrimaryOwner().getName(), "Transferencia de prueba", new Money(new BigDecimal("100")));
            transfer1.setDate(LocalDateTime.now().minusDays(2));
            transferRepository.save(transfer1);

        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("300"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("juan_an_fe69")
                                .password("patata")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isUnavailableForLegalReasons()).andReturn();
        originAccount = accountRepository.findAll().get(0);
        assertEquals(Status.FROZEN, ((Checking) originAccount).getStatus());
    }
    @Test
    void transfer_FraudDetectedLastSecond_exception() throws Exception {
        List<Account> accountList = accountRepository.findAll();
        Account originAccount = accountList.get(0);
        Account destinationAccount = accountList.get(2);

            // Previous transfers to check fraud
            Transfer transfer1 = new Transfer(originAccount, destinationAccount, destinationAccount.getPrimaryOwner().getName(), "Transferencia de prueba", new Money(new BigDecimal("100")));
            Transfer transfer2 = new Transfer(originAccount, destinationAccount, destinationAccount.getPrimaryOwner().getName(), "Transferencia de prueba", new Money(new BigDecimal("100")));
            transferRepository.saveAll(List.of(transfer1, transfer2));

        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("100"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("juan_an_fe69")
                                .password("patata")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isUnavailableForLegalReasons()).andReturn();
        originAccount = accountRepository.findAll().get(0);
        assertEquals(Status.FROZEN, ((Checking) originAccount).getStatus());
    }
    @Test
    void transfer_OriginIsFrozen_exception() throws Exception {
        List<Account> accountList = accountRepository.findAll();
        Account originAccount = accountList.get(0);
        ((Checking) originAccount).setStatus(Status.FROZEN);
        accountRepository.save(originAccount);
        Account destinationAccount = accountList.get(1);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("juan_an_fe69")
                                .password("patata")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isUnavailableForLegalReasons()).andReturn();
        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transfer_DestinationIsFrozen_exception() throws Exception {
        List<Account> accountList = accountRepository.findAll();
        Account originAccount = accountList.get(0);
        Account destinationAccount = accountList.get(1);
        ((StudentChecking) destinationAccount).setStatus(Status.FROZEN);
        accountRepository.save(destinationAccount);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("juan_an_fe69")
                                .password("patata")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isUnavailableForLegalReasons()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transfer_ValidDTO_transferDoneAndOriginReachMinimumBalance() throws Exception {
        List<Account> accountList = accountRepository.findAll();
        Account originAccount = accountList.get(0);
        Account destinationAccount = accountList.get(1);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("15513"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("juan_an_fe69")
                                .password("patata")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isCreated()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(1, transferList.size());
        assertTrue(result.getResponse().getContentAsString().contains("\"belowMinimumBalance\":true"));
        assertTrue(result.getResponse().getContentAsString().contains("\"amount\":209.00")); // After transfer, balance should be 209: 249 - 40 from Penalty Fee.
        originAccount = accountRepository.findAll().get(0);
        assertEquals(new BigDecimal("209.00"), originAccount.getBalance().getAmount());
    }
    @Test
    void transfer_ValidDTO_transferDoneAndDestinationGoesAboveMinimumBalance() throws Exception {
        List<Account> accountList = accountRepository.findAll();
        Account originAccount = accountList.get(0);
        Account destinationAccount = accountList.get(2);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("15513"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("juan_an_fe69")
                                .password("patata")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isCreated()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(1, transferList.size());
        System.out.println(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("\"belowMinimumBalance\":true")); // Origin is under minimum
        assertTrue(result.getResponse().getContentAsString().contains("\"belowMinimumBalance\":false")); // Destination is now above minimum
    }


    // TRANSFERS TO THIRD PARTIES TESTS:

    @Test
    void transferToThirdParty_ValidAccountsValidKeysValidDTO_transfer() throws Exception {
        Account originAccount = accountRepository.findAll().get(0);
        ThirdParty destinationAccount = thirdPartyRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-to-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(1, transferList.size());
        assertEquals(new BigDecimal("200.00"), transferList.get(0).getAmount().getAmount());
        originAccount = accountRepository.findAll().get(0);
        assertEquals(new BigDecimal("15562.00"), originAccount.getBalance().getAmount());
    }
    @Test
    void transferToThirdParty_OriginAccountDoesntExist_exception() throws Exception {
        ThirdParty destinationAccount = thirdPartyRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(0L, destinationAccount.getId(), destinationAccount.getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-to-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transferToThirdParty_DestinationAccountDoesntExist_exception() throws Exception {
        Account originAccount = accountRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), 0L, "Name", "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-to-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transferToThirdParty_DestinationNameIsNotDestinationOwner_exception() throws Exception {
        Account originAccount = accountRepository.findAll().get(0);
        ThirdParty destinationAccount = thirdPartyRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), "wrong name", "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-to-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transferToThirdParty_WrongHashKey_exception() throws Exception {
        Account originAccount = accountRepository.findAll().get(0);
        ThirdParty destinationAccount = thirdPartyRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-to-third-party/wrongHashKey/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transferToThirdParty_WrongSecretKey_exception() throws Exception {
        Account originAccount = accountRepository.findAll().get(0);
        ThirdParty destinationAccount = thirdPartyRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-to-third-party/siberiano/wrongSecretKey")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transferToThirdParty_NotEnoughFunds_exception() throws Exception {
        Account originAccount = accountRepository.findAll().get(0);
        ThirdParty destinationAccount = thirdPartyRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getName(), "De ti para mi", new BigDecimal("50000"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-to-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transferToThirdParty_FraudDetected150percent_exception() throws Exception {
        List<Account> accounts = accountRepository.findAll();
        Account originAccount = accounts.get(0);
        ThirdParty destinationAccount = thirdPartyRepository.findAll().get(0);

            // Previous transfers to check fraud
            Transfer transfer1 = new Transfer(originAccount, accounts.get(2), accounts.get(2).getPrimaryOwner().getName(), "Transferencia de prueba", new Money(new BigDecimal("100")));
            transfer1.setDate(LocalDateTime.now().minusDays(2));
            transferRepository.save(transfer1);

        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getName(), "De ti para mi", new BigDecimal("300"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-to-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnavailableForLegalReasons()).andReturn();
        originAccount = accountRepository.findAll().get(0);
        assertEquals(Status.FROZEN, ((Checking) originAccount).getStatus());
    }
    @Test
    void transferToThirdParty_FraudDetectedLastSecond_exception() throws Exception {
        List<Account> accountList = accountRepository.findAll();
        Account originAccount = accountList.get(0);
        ThirdParty destinationAccount = thirdPartyRepository.findAll().get(0);

        // Previous transfers to check fraud
        Transfer transfer1 = new Transfer(originAccount, accountList.get(2), accountList.get(2).getPrimaryOwner().getName(), "Transferencia de prueba", new Money(new BigDecimal("100")));
        Transfer transfer2 = new Transfer(originAccount, accountList.get(2), accountList.get(2).getPrimaryOwner().getName(), "Transferencia de prueba", new Money(new BigDecimal("100")));
        transferRepository.saveAll(List.of(transfer1, transfer2));

        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getName(), "De ti para mi", new BigDecimal("100"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-to-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnavailableForLegalReasons()).andReturn();
        originAccount = accountRepository.findAll().get(0);
        assertEquals(Status.FROZEN, ((Checking) originAccount).getStatus());
    }
    @Test
    void transferToThirdParty_FrozenOrigin_exception() throws Exception {
        Account originAccount = accountRepository.findAll().get(0);
        ((Checking) originAccount).setStatus(Status.FROZEN);
        originAccount = accountRepository.save(originAccount);
        ThirdParty destinationAccount = thirdPartyRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-to-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnavailableForLegalReasons()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transferToThirdParty_OriginReachsMinimumBalance_transferAndPenaltyApplied() throws Exception {
        Account originAccount = accountRepository.findAll().get(0);
        ThirdParty destinationAccount = thirdPartyRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getName(), "De ti para mi", new BigDecimal("15513"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-to-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(1, transferList.size());
        assertTrue(result.getResponse().getContentAsString().contains("\"belowMinimumBalance\":true"));
        assertTrue(result.getResponse().getContentAsString().contains("\"amount\":209.00")); // After transfer, balance should be 209: 249 - 40 from Penalty Fee.
        originAccount = accountRepository.findAll().get(0);
        assertEquals(new BigDecimal("209.00"), originAccount.getBalance().getAmount()); // After transfer, balance should be 209: 249 - 40 from Penalty Fee.
    }
    @Test
    void transferToThirdParty_NegativeToCreditLimit_newTransfer() throws Exception {     // A Credit card balance can be negative from 0 to "Negative Credit Limit".
        Account originAccount = accountRepository.findAll().get(3);
        ThirdParty destinationAccount = thirdPartyRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getName(), "De ti para mi", new BigDecimal("1550"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-to-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("\"amount\":-50.00")); // A Credit card balance can be negative from 0 to "Negative Credit Limit".

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(1, transferList.size());
        originAccount = accountRepository.findAll().get(3);
        assertEquals(new BigDecimal("-50.00"), originAccount.getBalance().getAmount());
    }
    @Test
    void transferToThirdParty_NotEnoughFundsBeyondCreditLimit_exception() throws Exception {     // A Credit card balance can't be negative beyond Credit Limit.
        Account originAccount = accountRepository.findAll().get(3);
        ThirdParty destinationAccount = thirdPartyRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getName(), "De ti para mi", new BigDecimal("2500"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-to-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }

    // TRANSFERS FROM THIRD PARTIES TESTS:

    @Test
    void transferFromThirdParty_ValidAccountsValidKeysValidDTO_transfer() throws Exception {
        ThirdParty originAccount = thirdPartyRepository.findAll().get(0);
        Account destinationAccount = accountRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-from-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(1, transferList.size());
        assertEquals(new BigDecimal("200.00"), transferList.get(0).getAmount().getAmount());
        destinationAccount = accountRepository.findAll().get(0);
        assertEquals(new BigDecimal("15962.00"), destinationAccount.getBalance().getAmount());
    }
    @Test
    void transferFromThirdParty_OriginAccountDoesntExist_exception() throws Exception {
        Account destinationAccount = accountRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(0L, destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-from-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transferFromThirdParty_DestinationAccountDoesntExist_exception() throws Exception {
        ThirdParty originAccount = thirdPartyRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), 0L, "Name", "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-from-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transferFromThirdParty_DestinationNameIsNotDestinationOwner_exception() throws Exception {
        ThirdParty originAccount = thirdPartyRepository.findAll().get(0);
        Account destinationAccount = accountRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), "wrong name", "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-from-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transferFromThirdParty_WrongHashKey_exception() throws Exception {
        ThirdParty originAccount = thirdPartyRepository.findAll().get(0);
        Account destinationAccount = accountRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-from-third-party/wrongHashKey/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transferFromThirdParty_WrongSecretKey_exception() throws Exception {
        ThirdParty originAccount = thirdPartyRepository.findAll().get(0);
        Account destinationAccount = accountRepository.findAll().get(0);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-from-third-party/siberiano/wrongSecretKey")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transferFromThirdParty_FraudDetected150percent_exception() throws Exception {
        ThirdParty originAccount = thirdPartyRepository.findAll().get(0);
        List<Account> accounts = accountRepository.findAll();
        Account destinationAccount = accounts.get(0);

            // Previous transfers to check fraud
            Transfer transfer1 = new Transfer(accounts.get(0), accounts.get(2), accounts.get(2).getPrimaryOwner().getName(), "Transferencia de prueba", new Money(new BigDecimal("100")));
            transfer1.setDate(LocalDateTime.now().minusDays(2));
            transferRepository.save(transfer1);

        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("300"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-from-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnavailableForLegalReasons()).andReturn();
        destinationAccount = accountRepository.findAll().get(0);
        assertEquals(Status.FROZEN, ((Checking) destinationAccount).getStatus());
    }
    @Test
    void transferFromThirdParty_FraudDetectedLastSecond_exception() throws Exception {
        ThirdParty originAccount = thirdPartyRepository.findAll().get(0);
        List<Account> accountList = accountRepository.findAll();
        Account destinationAccount = accountList.get(0);

        // Previous transfers to check fraud
        Transfer transfer1 = new Transfer(accountList.get(0), accountList.get(2), accountList.get(2).getPrimaryOwner().getName(), "Transferencia de prueba", new Money(new BigDecimal("100")));
        Transfer transfer2 = new Transfer(accountList.get(0), accountList.get(2), accountList.get(2).getPrimaryOwner().getName(), "Transferencia de prueba", new Money(new BigDecimal("100")));
        transferRepository.saveAll(List.of(transfer1, transfer2));

        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("100"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-from-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnavailableForLegalReasons()).andReturn();
        destinationAccount = accountRepository.findAll().get(0);
        assertEquals(Status.FROZEN, ((Checking) destinationAccount).getStatus());
    }
    @Test
    void transferFromThirdParty_FrozenDestination_exception() throws Exception {
        ThirdParty originAccount = thirdPartyRepository.findAll().get(0);
        Account destinationAccount = accountRepository.findAll().get(0);
        ((Checking) destinationAccount).setStatus(Status.FROZEN);
        destinationAccount = accountRepository.save(destinationAccount);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-from-third-party/siberiano/potatomix")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnavailableForLegalReasons()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(0, transferList.size());
    }
    @Test
    void transferFromThirdParty_ValidDTO_transferDoneAndDestinationGoesAboveMinimumBalance() throws Exception {
        ThirdParty originAccount = thirdPartyRepository.findAll().get(0);
        Account destinationAccount = accountRepository.findAll().get(2);
        TransferDTO transferDTO = new TransferDTO(originAccount.getId(), destinationAccount.getId(), destinationAccount.getPrimaryOwner().getName(), "De ti para mi", new BigDecimal("200"));

        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult result = mockMvc.perform(
                post("/transfer-from-third-party/siberiano/caragorrino")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        List<Transfer> transferList = transferRepository.findAll();
        assertEquals(1, transferList.size());
        System.out.println(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("\"belowMinimumBalance\":false")); // Destination is now above minimum
    }

}