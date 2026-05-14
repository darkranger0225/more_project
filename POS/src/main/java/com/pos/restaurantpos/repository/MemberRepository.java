package com.pos.restaurantpos.repository;

import com.pos.restaurantpos.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByPhone(String phone);

    boolean existsByPhone(String phone);

    List<Member> findByStatus(Member.Status status);

    List<Member> findByPhoneContaining(String phone);
}
