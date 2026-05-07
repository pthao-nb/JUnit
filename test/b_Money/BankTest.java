package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		assertEquals("SweBank", SweBank.getName());
		assertEquals("Nordea", Nordea.getName());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SweBank.getCurrency());
		assertEquals(DKK, DanskeBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		// Thử mở một tài khoản mới và kiểm tra xem nó có tồn tại không (bằng cách lấy số dư)
		SweBank.openAccount("Vinh");
		assertNotNull(SweBank.getBalance("Vinh"));
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		// Nạp tiền cho Ulrika (tài khoản đã mở ở setUp)
		SweBank.deposit("Ulrika", new Money(1000, SEK));
		assertEquals((Integer)1000, SweBank.getBalance("Ulrika"));
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		// Nạp trước rồi rút sau
		SweBank.deposit("Ulrika", new Money(1000, SEK));
		SweBank.withdraw("Ulrika", new Money(400, SEK));
		assertEquals((Integer)600, SweBank.getBalance("Ulrika"));
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		// Kiểm tra số dư mặc định của tài khoản mới mở (thường là 0)
		assertEquals((Integer)0, SweBank.getBalance("Bob"));
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		// Chuyển tiền từ SweBank (Ulrika) sang Nordea (Bob)
		SweBank.deposit("Ulrika", new Money(1000, SEK));
		SweBank.transfer("Ulrika", Nordea, "Bob", new Money(500, SEK));
		
		assertEquals((Integer)500, SweBank.getBalance("Ulrika"));
		assertEquals((Integer)500, Nordea.getBalance("Bob"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		// Kiểm tra xem lệnh tick() có hoạt động không
		SweBank.addTimedPayment("Ulrika", "t1", 1, 0, new Money(100, SEK), Nordea, "Bob");
		SweBank.deposit("Ulrika", new Money(1000, SEK));
				
		SweBank.tick();
				
		// Ulrika mất 100, Bob nhận được 100
		assertEquals((Integer)900, SweBank.getBalance("Ulrika"));
		assertEquals((Integer)100, Nordea.getBalance("Bob"));
	}
}
