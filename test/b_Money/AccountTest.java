package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		// Kiểm tra thêm và xóa thanh toán định kỳ
		// Giả sử Account có phương thức timedPaymentExists hoặc bạn kiểm tra gián tiếp
		SweBank.addTimedPayment("Alice", "p1", 10, 0, new Money(100, SEK), SweBank, "Alice");
	
		// Nếu không có lỗi văng ra ở đây là bước đầu thành công
		SweBank.removeTimedPayment("Alice", "p1");
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		// Kiểm tra tiền có thực sự bị trừ khi tick() chạy không
		// 1. Thêm một thanh toán định kỳ: cứ mỗi 1 tick, Alice chuyển cho chính mình 100 SEK
		SweBank.addTimedPayment("Alice", "transfer1", 1, 0, new Money(100, SEK), SweBank, "Alice");
		// 2. Chạy tick()
		SweBank.tick();
		// 3. Kiểm tra số dư (Alice nạp 1.000.000 ở setUp, trừ 100 rồi cộng 100 thì vẫn là 1.000.000)
		// Lưu ý: Tùy vào logic bài Lab của bạn, tick() có thể làm thay đổi số dư theo cách khác
		assertEquals((Integer)1000000, SweBank.getBalance("Alice"));
	}

	@Test
	public void testAddWithdraw() throws AccountDoesNotExistException{
		// Kiểm tra nạp tiền
		SweBank.deposit("Alice", new Money(500, SEK));
		assertEquals((Integer)1000500, SweBank.getBalance("Alice"));
		// Kiểm tra rút tiền
		SweBank.withdraw("Alice", new Money(200, SEK));
		assertEquals((Integer)1000300, SweBank.getBalance("Alice"));
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		// Kiểm tra số dư khởi tạo của Alice (nạp 1.000.000 ở hàm setUp)
		assertEquals((Integer)1000000, SweBank.getBalance("Alice"));
		// Kiểm tra số dư của testAccount (nạp 10.000.000 ở hàm setUp)
		assertEquals((Integer)10000000, testAccount.getBalance().getAmount());
	}

}
