package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		// Kiểm tra xem phương thức getName() có trả về đúng tên viết tắt của tiền tệ đã thiết lập không.
		assertEquals("SEK", SEK.getName());
		assertEquals("DKK", DKK.getName());
		assertEquals("EUR", EUR.getName());
	}
	
	@Test
	public void testGetRate() {
		// Kiểm tra tỷ giá đã thiết lập ở hàm setUp
		assertEquals(0.15, SEK.getRate(), 0.001);
		assertEquals(0.20, DKK.getRate(), 0.001);
	}
	
	@Test
	public void testSetRate() {
		// Thử thay đổi tỷ giá và kiểm tra lại
		SEK.setRate(0.25);
		assertEquals(0.25, SEK.getRate(), 0.001);
	
		// Trả lại tỷ giá cũ để không ảnh hưởng bài test khác
		SEK.setRate(0.15);
	}
	
	@Test
	public void testGlobalValue() {
		// Kiểm tra giá trị quy đổi ra đơn vị chung (thường là nhân amount với rate)
		// amount * rate, ví dụ: 100 SEK * 0.15 = 15
		assertEquals((Integer)15, SEK.universalValue(100));

		// 100 EUR * 1.5 = 150
		assertEquals((Integer)150, EUR.universalValue(100));
	}
	
	@Test
	public void testValueInThisCurrency() {
		// Chuyển đổi từ tiền tệ khác sang tiền tệ này
		// Công thức thường là: (amount * foreign_rate) / local_rate
		// Ví dụ từ 100 DKK sang SEK: (100 * 0.20) / 0.15 = 133.33... -> 133
		assertEquals((Integer)133, SEK.valueInThisCurrency(100, DKK));
	}

}
