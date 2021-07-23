package hello.servlet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;


class MemberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance();

    @AfterEach
    void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Member member = new Member("hello",20);

        //when
        Member saveMember = memberRepository.save(member);

        //then
        Member findMember = memberRepository.findById(saveMember.getId());
        Assertions.assertThat(findMember).isEqualTo(saveMember);
    }

    @Test
    void findAll(){
        //given
        Member member1 = new Member("member1",21);
        Member member2 = new Member("member2",22);

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);

        //then
        List<Member> result = memberRepository.findAll();
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result).contains(member1,member2);


    }

}