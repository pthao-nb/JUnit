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
		// Kiểm tra xem ngân hàng có trả về đúng tên đã thiết lập trong hàm setUp không
		assertEquals("SweBank", SweBank.getName());
		assertEquals("Nordea", Nordea.getName());
	}

	@Test
	public void testGetCurrency() {
		// Đảm bảo ngân hàng sử dụng đúng loại tiền tệ cơ sở
		// SweBank dùng SEK, DanskeBank dùng DKK như đã định nghĩa ở setUp
		assertEquals(SEK, SweBank.getCurrency());
		assertEquals(DKK, DanskeBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		// Thử mở một tài khoản mới và kiểm tra xem nó có tồn tại không 
		// Nếu tài khoản "Vinh" được mở thành công, hàm getBalance sẽ không bị lỗi Null
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
		// Kiểm tra tính năng rút tiền
		// Nạp vào 1000, rút ra 400, số dư còn lại phải là 600
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
		// Kiểm tra việc chuyển tiền liên ngân hàng (từ SweBank sang Nordea)
		// Ulrika nạp 1000, chuyển cho Bob 500 -> Cả hai phải có số dư là 500
		SweBank.deposit("Ulrika", new Money(1000, SEK));
		SweBank.transfer("Ulrika", Nordea, "Bob", new Money(500, SEK));
		
		assertEquals((Integer)500, SweBank.getBalance("Ulrika"));
		assertEquals((Integer)500, Nordea.getBalance("Bob"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		// Kiểm tra hệ thống thanh toán định kỳ.
		// Thiết lập lệnh chuyển 100 SEK từ Ulrika sang Bob mỗi khi tick() được gọi
		SweBank.addTimedPayment("Ulrika", "t1", 1, 0, new Money(100, SEK), Nordea, "Bob");
		SweBank.deposit("Ulrika", new Money(1000, SEK));
				
		// Kích hoạt một đơn vị thời gian
		SweBank.tick();
				
		// Sau khi tick, Ulrika phải bị trừ 100, Bob phải nhận được 100
		assertEquals((Integer)900, SweBank.getBalance("Ulrika"));
		assertEquals((Integer)100, Nordea.getBalance("Bob"));
	}
}
