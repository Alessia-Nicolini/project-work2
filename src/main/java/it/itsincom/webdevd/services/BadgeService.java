package it.itsincom.webdevd.services;

import it.itsincom.webdevd.repositories.BadgeRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;

@ApplicationScoped
public class BadgeService {
    private static final int TOTAL_BADGES = 30;

    private final BadgeRepository badgeRepository;

    public BadgeService(BadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    public Map<String, Integer> getBadgeStats() {
        int availableBadges = badgeRepository.countAvailableBadges();
        return Map.of("available", availableBadges, "total", TOTAL_BADGES);
    }
}
