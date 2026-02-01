package com.ok.tickets.filters;

import com.ok.tickets.domain.User;
import com.ok.tickets.repos.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
//@RequiredArgsConstructor
public class UserProvisioningFilter  extends OncePerRequestFilter {

	private final UserRepo userRepo;

	public UserProvisioningFilter(UserRepo userRepo) {this.userRepo = userRepo;}

	@Override
	protected void doFilterInternal(
					HttpServletRequest request,
					HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {

		Authentication authentication =
						SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null
					&& authentication.isAuthenticated()
					&& authentication.getPrincipal() instanceof Jwt jwt) {


			UUID keycloakId = UUID.fromString(jwt.getSubject());

			if (!userRepo.existsById(keycloakId)) { //? if user not exist in DB

				User user = new User();
				user.setId(keycloakId);
				user.setName(jwt.getClaimAsString("preferred_username"));
				user.setEmail(jwt.getClaimAsString("email"));

				userRepo.save(user);                  //? save user in DB

				filterChain.doFilter(request, response);

			}

		}
	}
}
