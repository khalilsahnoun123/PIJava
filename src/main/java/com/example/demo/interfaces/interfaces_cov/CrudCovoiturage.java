package com.example.demo.interfaces.interfaces_cov;

import com.example.demo.models.models_cov.Covoiturage;

import java.sql.SQLException;
import java.util.List;

public interface CrudCovoiturage <Cov> {
    public void ajouter(Cov c);
    public void modifier(Cov c);
    public void supprimer(int id) throws SQLException;
    public List<Covoiturage> Show();
}
