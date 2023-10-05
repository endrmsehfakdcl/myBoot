package com.keduit.entity;

import com.keduit.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
public class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("member auditing 테스트")
    @WithMockUser(username = "gildong", roles = "USER")
    public void auditingTest() {
        Member member = new Member();
        memberRepository.save(member);

        entityManager.flush();
        entityManager.clear();

        Member member1 = memberRepository.findById(member.getId())
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("member1 = " + member1);
        System.out.println("member1.getRegTime() = " + member1.getRegTime());
        System.out.println("member1.getUpdateTime() = " + member1.getUpdateTime());
        System.out.println("member1.getCreateBy() = " + member1.getCreatedBy());
        System.out.println("member1.getModifiedBy() = " + member1.getModifiedBy());
    }

}
