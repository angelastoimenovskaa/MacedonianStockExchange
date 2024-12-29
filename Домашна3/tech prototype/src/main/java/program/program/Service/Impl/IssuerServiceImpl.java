package program.program.Service.Impl;

import org.springframework.stereotype.Service;
import program.program.Model.Issuer;
import program.program.Service.IssuerService;
import program.program.repository.IssuersRepository;

import java.util.List;


@Service
public class IssuerServiceImpl implements IssuerService {

    IssuersRepository repo;

    public IssuerServiceImpl(IssuersRepository repo) {
        this.repo = repo;
    }

    @Override
    public Issuer getByName(String name) {
        return repo.getByName(name);
    }

    @Override
    public List<Issuer> listAll() {
        return repo.listAll();
    }

    @Override
    public Issuer getById(int id) {
        return repo.getById(id);
    }
}
