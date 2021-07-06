package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class RateDiscountPolicyTest {

    DiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 적용되어야 함")
    void vip_o(){
        //10% 할인이 잘 적용 됐는지 확인
        //given
        Member member = new Member(1L,"memberVIP", Grade.VIP);

        //when
        int discount = discountPolicy.Discount(member,10000);

        //then
        assertThat(discount).isEqualTo(1000);
    }

    @Test
    @DisplayName("VIP가 아니면 할인이 적용되지 않아야 함")
    void vip_x(){
        //10% 할인이 잘 적용 됐는지 확인
        //given
        Member member = new Member(1L,"memberBasic", Grade.BASIC);

        //when
        int discount = discountPolicy.Discount(member,10000);

        //then
        assertThat(discount).isEqualTo(1000);
    }

}