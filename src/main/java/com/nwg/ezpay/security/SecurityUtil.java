package com.nwg.ezpay.security;

import com.nwg.ezpay.entity.AccountHolder;
import com.nwg.ezpay.service.AccountHolderService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility class for resolving the currently authenticated account holder
 * from the Spring Security context.
 *
 * Author: Muskan
 * Version: 1.0
 * Since: 2025-09-07
 */
@Component
public class SecurityUtil {

    private static AccountHolderService accountHolderService;

    /**
     * Injects the AccountHolderService once at startup so we can use it
     * in a static context.
     *
     * @param service the AccountHolderService bean
     */
    public SecurityUtil(AccountHolderService service) {
        SecurityUtil.accountHolderService = service;
    }

    /**
     * Resolve the currently authenticated {@link AccountHolder} from
     * the Spring Security context.
     *
     * @return AccountHolder of the logged-in user
     * @throws IllegalStateException if no authentication is available
     */
    public static AccountHolder getCurrentAccountHolder() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        String username = auth.getName(); // principal’s username
        return accountHolderService.getByUsername(username);
    }
}
