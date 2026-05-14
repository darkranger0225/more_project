package com.pos.restaurantpos.service;

import com.pos.restaurantpos.entity.Member;
import com.pos.restaurantpos.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public List<Member> findEnabled() {
        return memberRepository.findByStatus(Member.Status.ENABLED);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("会员不存在"));
    }

    public Member findByPhone(String phone) {
        return memberRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("会员不存在"));
    }

    public boolean existsByPhone(String phone) {
        return memberRepository.existsByPhone(phone);
    }

    @Transactional
    public Member createMember(Member member) {
        if (memberRepository.existsByPhone(member.getPhone())) {
            throw new RuntimeException("该手机号已注册");
        }
        if (!isValidPhone(member.getPhone())) {
            throw new RuntimeException("手机号格式不正确");
        }
        member.setPoints(100);
        member.setStatus(Member.Status.ENABLED);
        return memberRepository.save(member);
    }

    @Transactional
    public Member updateMember(Long id, Member member) {
        Member existingMember = findById(id);
        if (!existingMember.getPhone().equals(member.getPhone())) {
            if (memberRepository.existsByPhone(member.getPhone())) {
                throw new RuntimeException("该手机号已被使用");
            }
            if (!isValidPhone(member.getPhone())) {
                throw new RuntimeException("手机号格式不正确");
            }
            existingMember.setPhone(member.getPhone());
        }
        existingMember.setName(member.getName());
        return memberRepository.save(existingMember);
    }

    @Transactional
    public void addPoints(Long memberId, BigDecimal amount) {
        Member member = findById(memberId);
        int pointsToAdd = amount.multiply(new BigDecimal("5")).intValue();
        member.setPoints(member.getPoints() + pointsToAdd);
        memberRepository.save(member);
    }

    @Transactional
    public BigDecimal usePoints(Long memberId, int points) {
        Member member = findById(memberId);
        if (member.getPoints() < points) {
            throw new RuntimeException("积分不足");
        }
        member.setPoints(member.getPoints() - points);
        memberRepository.save(member);
        return new BigDecimal(points).divide(new BigDecimal("100"));
    }

    @Transactional
    public void enableMember(Long id) {
        Member member = findById(id);
        member.setStatus(Member.Status.ENABLED);
        memberRepository.save(member);
    }

    @Transactional
    public void disableMember(Long id) {
        Member member = findById(id);
        member.setStatus(Member.Status.DISABLED);
        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^1[3-9]\\d{9}$");
    }
}
