package renewal.awesome_travel.Member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import jakarta.transaction.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class MemberTest {
    MemberDto memberDto = new MemberDto();

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    // 임시 회원 작성
    public MemberDto testMemberDto() {
        MemberDto memberDto = new MemberDto();
        memberDto.setEmail("test@test.com");
        memberDto.setName("테스트");
        memberDto.setNumber("010-1234-5678");
        return memberDto;
    }

    @Test
    @DisplayName("가입 테스트")
    public void saveMemberTest() {
        MemberDto memberDto = testMemberDto();

        // Mapper로 변환
        Member member = memberMapper.MemberDtoToMember(memberDto);

        System.out.println("save 전 : " + member.getId()); // -> null 출력
        memberRepository.save(member); // 저장 시점부터 영속성 부여 / id 할당
        System.out.println("save 후 : " + member.getId()); // -> 1 출력
        System.out.println();

        List<Member> dbmember = memberRepository.findAll(); // 1개만 나옴

        assertEquals(dbmember.size(), 1);
        Member member2 = dbmember.get(0);
        System.out.printf(
                "member1\nid : %s\nname : %s\nemail : %s\nnumber : %s\n\n",
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getNumber());

        System.out.printf(
                "member2\nid : %s\nname : %s\nemail : %s\nnumber : %s\n\n",
                member2.getId(),
                member2.getName(),
                member2.getEmail(),
                member2.getNumber());

        assertEquals(member, member2);
    }

    @Test
    @DisplayName("중복 테스트")
    public void duplicateMemberTest() {
        MemberDto memberDto = testMemberDto();

        // Mapper로 변환
        Member member1 = memberMapper.MemberDtoToMember(memberDto);

        memberRepository.save(member1); // 1차 저장

        // email 제외 변경
        memberDto.setName("TEST2");
        memberDto.setNumber("010-9999-9999");
        Member member2 = memberMapper.MemberDtoToMember(memberDto);

        memberRepository.save(member2); // 2차 저장 (영속성 컨텍스트에만 반영 / 실제 반영 아직 안됨)

        Exception exception = assertThrows(Exception.class, () -> {
            memberRepository.flush(); // 실제 에러 발생 지점
        });

        System.out.printf("\n에러 내용 :\n %s\n\n",exception);

    }

}