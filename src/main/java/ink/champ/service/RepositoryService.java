package ink.champ.service;

import ink.champ.models.*;
import ink.champ.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepositoryService {

    @Autowired private SportRepository repSports;
    @Autowired private ChampRepository repChamps;
    @Autowired private TeamRepository repTeams;
    @Autowired private PlayerRepository repPlayers;
    @Autowired private UserRepository repUsers;

    public List<Champ> getChamps() { return repChamps.findAll(Sort.by(Sort.Direction.DESC, "id")); }
    public List<Sport> getSports() { return repSports.findAll(Sort.by(Sort.Direction.DESC, "id")); }
    public List<User> getUsers() { return repUsers.findAll(Sort.by(Sort.Direction.DESC, "id")); }
    public List<Team> getTeams() { return repTeams.findAll(Sort.by(Sort.Direction.DESC, "id")); }
    public List<Player> getPlayers() { return repPlayers.findAll(Sort.by(Sort.Direction.DESC, "id")); }

    public Sport getSportById(Long id) { return repSports.findById(id).get(); }
    public Champ getChampById(Long id) { return repChamps.findById(id).get(); }
    public Team getTeamById(Long id) { return repTeams.findById(id).get(); }
    public Player getPlayerById(Long id) { return repPlayers.findById(id).get(); }

    public User getUserByUsername(String username) {
        return repUsers.findByUsername(username);
    }

    public void addNewUser(User user) { repUsers.save(user); }
    public void addNewChamp(Champ champ) { repChamps.save(champ); }
    public void addNewTeam(Team team) { repTeams.save(team); }
    public void addNewPlayer(Player player) { repPlayers.save(player); }


//    public Champ getChampByIdOld(Long id) {
//        Optional<Users> users = usersRepository.findById(id);
//        ArrayList<Users> arrayList = new ArrayList<>();
//        users.ifPresent(arrayList::add);
//        model.addAttribute("champ", arrayList);
//        return repChamps.findById(id).get();
//    }
}
