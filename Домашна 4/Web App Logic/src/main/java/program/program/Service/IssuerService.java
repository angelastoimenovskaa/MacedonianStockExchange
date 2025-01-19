package program.program.Service;

import program.program.Model.Issuer;

import java.util.List;



public interface IssuerService {

    public Issuer getByName(String name);
    public List<Issuer> listAll();

    public Issuer getById(int id);

}
