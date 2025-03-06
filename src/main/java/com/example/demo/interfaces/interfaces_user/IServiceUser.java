package com.example.demo.interfaces.interfaces_user;

import java.util.List;

import com.example.demo.models.models_user.User;

public interface IServiceUser {
	
	void ajouter(User user);

	void supprimer(int id);

	void modifier(User user, int id);

	List<User> afficherTous();

	User rechercherUserParId(int id);

}
