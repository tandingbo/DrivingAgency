package com.beautifulsoup.driving.repository;

import com.beautifulsoup.driving.pojo.Agent;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AgentRepository extends JpaRepository<Agent,Integer> {
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @EntityGraph(value = "agent.all")
    List<Agent> findAll();

    Agent findAgentByAgentName(String agentName);
}
