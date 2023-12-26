package com.theonlysd12.iplogin;

import java.util.*;

public class IPStorer {
    private static final HashMap<String, Set<String>> IPStorerMap = new HashMap<>();

    public static void addPlayer(String username, String ip){
        Set<String> IPs = new HashSet<>();
        IPs.add(ip);
        IPStorerMap.put(username, IPs);
    }

    public static boolean containsPlayer(String username){
        return IPStorerMap.containsKey(username);
    }

    public static boolean containsIP(String ip){
        Set<String> IPs = new HashSet<>();
        IPs.add(ip);
        return IPStorerMap.containsValue(IPs);
    }

    public static void addPlayerIP(String username, String ip){
        Set<String> IPs = IPStorerMap.get(username);
        IPs.add(ip);
        IPStorerMap.put(username, IPs);
    }

    public static List<String> getPlayersFromIP(String ip){
        List<String> matchingUsernames = new LinkedList<>();
        for(HashMap.Entry<String, Set<String>> entry : IPStorerMap.entrySet()){
            Set<String> IPs = entry.getValue();
            if(IPs.contains(ip)){
                matchingUsernames.add(entry.getKey());
            }
        }
        return matchingUsernames;
    }

    public static List<String> getIPsFromPlayer(String username){
        return IPStorerMap.get(username).stream().toList();
    }

    public static void removeIPFromPlayer(String username, String ip){
        Set<String> IPs = IPStorerMap.get(username);
        IPs.remove(ip);
        IPStorerMap.put(username, IPs);
    }

    public static void changeIPFromPlayer(String username, String oldip, String newip){
        Set<String> IPs = IPStorerMap.get(username);
        IPs.remove(oldip);
        IPs.add(newip);
        IPStorerMap.put(username, IPs);
    }

    public static void clearIPsFromPlayer(String username){
        IPStorerMap.remove(username);
    }

    public static List<String> getPlayers(){
        return IPStorerMap.keySet().stream().toList();
    }
}
