package edu.cibertec.votoelectronico.helper;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptSecureHashing implements SecureHashingHelper {

	@Override
	public String hashPassword(String plainTextPassword) {
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
	}

	@Override
	public boolean checkPass(String plainPassword, String hashedPassword) {
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}

}
