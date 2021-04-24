package ink.champ.service;

import ink.champ.models.Champ;
import ink.champ.models.Sport;
import ink.champ.models.User;
import ink.champ.repository.ChampRepository;
import ink.champ.repository.SportRepository;
import ink.champ.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepositoryService {

    @Autowired private ChampRepository repChamps;
    @Autowired private SportRepository repSports;
    @Autowired private UserRepository repUsers;

    public List<Champ> getChamps() { return repChamps.findAll(Sort.by(Sort.Direction.DESC, "id")); }
    public List<Sport> getSports() { return repSports.findAll(); }
    public List<User> getUsers() { return repUsers.findAll(); }
    public Champ getChampById(Long id) {
//        Optional<Users> users = usersRepository.findById(id);
//        ArrayList<Users> arrayList = new ArrayList<>();
//        users.ifPresent(arrayList::add);
//        model.addAttribute("champ", arrayList);
        return repChamps.findById(id).get();
    }

    public User getUserByUsername(String username) {
        return repUsers.findByUsername(username);
    }

    public void addNewChamp(Champ champ) { repChamps.save(champ); }
    public void addNewUser(User user) { repUsers.save(user); }

//    public User getUserByEmail(String email) {
//        return repUsers.
//    }
}
