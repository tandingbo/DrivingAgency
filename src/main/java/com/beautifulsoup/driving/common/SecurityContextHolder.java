package com.beautifulsoup.driving.common;

import com.beautifulsoup.driving.pojo.Agent;

public class SecurityContextHolder {
    public static final ThreadLocal<Agent> agentHolder=new ThreadLocal<>();

    public static void addAgent(Agent agent){
        agentHolder.set(agent);
    }

    public static Agent getAgent(){
        return agentHolder.get();
    }

    public static void remove(){
        agentHolder.remove();
    }
}
