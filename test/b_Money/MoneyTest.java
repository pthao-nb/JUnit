package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		// Kiểm tra xem phương thức getAmount() có trả về đúng giá trị số nguyên ban đầu không
		assertEquals((Integer)10000, SEK100.getAmount());
		assertEquals((Integer)1000, EUR10.getAmount());
	}

	@Test
	public void testGetCurrency() {
		// Đảm bảo đối tượng Money lưu giữ đúng loại tiền tệ (Currency) đã được khởi tạo
		assertEquals(SEK, SEK100.getCurrency());
		assertEquals(EUR, EUR10.getCurrency());
	}

	@Test
	public void testToString() {
		// Kiểm tra định dạng chuỗi trả về 
		// Theo quy ước, 10000 sẽ được hiển thị là 100.0 (chia cho 100)
		assertEquals("100.0 SEK", SEK100.toString());
		assertEquals("10.0 EUR", EUR10.toString());
	}

	@Test
	public void testGlobalValue() {
		// Ghi chú: Kiểm tra giá trị quy đổi chung
		// Ví dụ: 10000 SEK * tỷ giá 0.15 = 1500 
		assertEquals((Integer)1500, SEK100.universalValue());
	}

	@Test
	public void testEqualsMoney() {
		// Tạo một đối tượng tiền mới có cùng giá trị với SEK100
		Money anotherSEK100 = new Money(10000, SEK);
				
		// 1. Kiểm tra hai đối tượng giống hệt nhau -> Phải True
	    assertTrue(SEK100.equals(anotherSEK100));

	    // 2. Kiểm tra SEK100 (1500) và EUR10 (1500) -> Chúng BẰNG NHAU nên phải dùng assertTrue
	    assertTrue(SEK100.equals(EUR10));

	    // 3. Kiểm tra trường hợp khác giá trị (1500 so với 3000) -> Phải False
	    assertFalse(SEK100.equals(EUR20));
	}

	@Test
	public void testAdd() {
		// Kiểm tra phép cộng tiền. 10000 + 20000 phải bằng 30000 (cùng đơn vị SEK)
		Money result = SEK100.add(SEK200);
		assertEquals((Integer)30000, result.getAmount());
	}

	@Test
	public void testSub() {
		// Kiểm tra phép trừ tiền. 20000 - 10000 phải còn lại 10000 SEK
		Money result = SEK200.sub(SEK100);
		assertEquals((Integer)10000, result.getAmount());
	}

	@Test
	public void testIsZero() {
		// Kiểm tra hàm nhận diện giá trị bằng 0
		assertTrue(SEK0.isZero());
		assertFalse(SEK100.isZero());
	}

	@Test
	public void testNegate() {
		// Kiểm tra hàm đảo ngược giá trị (số dương thành số âm)
		Money negated = SEK100.negate();
		assertEquals((Integer)(-10000), negated.getAmount());
	}

	@Test
	public void testCompareTo() {
		// Kiểm tra thứ tự so sánh dựa trên giá trị Universal.
		// SEK100 (1500) nhỏ hơn EUR20 (3000) nên trả về số âm.
		assertTrue(SEK100.compareTo(EUR20) < 0);
		
		// EUR20 (3000) lớn hơn SEK100 (1500) nên trả về số dương
		assertTrue(EUR20.compareTo(SEK100) > 0);
		
		// EUR20 (3000) lớn hơn SEK100 (1500) nên trả về số dương
		assertEquals(0, SEK100.compareTo(new Money(10000, SEK)));
	}
}
