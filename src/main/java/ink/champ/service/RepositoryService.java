package ink.champ.service;

import ink.champ.models.*;
import ink.champ.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryService {

    @Autowired private SportRepository sports;
    @Autowired private ChampRepository champs;
    @Autowired private ChampTeamRepository champTeams;
    @Autowired private ChampRoleRepository champRoles;
    @Autowired private TeamRepository teams;
    @Autowired private TeamPlayerRepository teamPlayers;
    @Autowired private TeamRoleRepository teamRoles;
    @Autowired private PlayerRepository players;
    @Autowired private PlayerRoleRepository playerRoles;
    @Autowired private UserRepository users;

    private Sort sortDescId = Sort.by(Sort.Direction.DESC, "id");

    public List<User> getUsers() { return users.findAll(sortDescId); }
    public List<Sport> getSports() { return sports.findAll(sortDescId); }
    public List<Champ> getChamps() { return champs.findAll(sortDescId); }
    public List<Team> getTeams() { return teams.findAll(sortDescId); }
    public List<Player> getPlayers() { return players.findAll(sortDescId); }

    public List<Champ> getUserChamps(User user) { return champs.findChampsByUser(user, sortDescId); }
    public List<Team> getUserTeams(User user) { return teams.findTeamsByUser(user, sortDescId); }
    public List<Player> getUserPlayers(User user) { return players.findPlayersByUser(user, sortDescId); }
    public List<ChampTeam> getChampTeams(Champ champ) { return champTeams.findChampTeamsByChamp(champ, sortDescId); }
    public List<TeamPlayer> getTeamPlayers(Team team) { return teamPlayers.findTeamPlayersByTeam(team, sortDescId); }

    public Sport getSportById(Long id) { return sports.findById(id).get(); }
    public Champ getChampById(Long id) { return champs.findById(id).get(); }
    public Team getTeamById(Long id) { return teams.findById(id).get(); }
    public Player getPlayerById(Long id) { return players.findById(id).get(); }

    public User getUserByUsername(String username) { return users.findByUsername(username); }

    public void addNewUser(User user) { users.save(user); }
    public void addNewChamp(Champ champ) { champs.save(champ); }
    public void addNewTeam(Team team) { teams.save(team); }
    public void addNewPlayer(Player player) { players.save(player); }
    public void addNewTeamPlayer(TeamPlayer teamPlayer) { teamPlayers.save(teamPlayer); }
    public void addNewChampTeam(ChampTeam champTeam) { champTeams.save(champTeam); }
    public void addNewChampRole(ChampRole role) { champRoles.save(role); }
    public void addNewTeamRole(TeamRole role) { teamRoles.save(role); }
    public void addNewPlayerRole(PlayerRole role) { playerRoles.save(role); }

    public void saveChampRole(ChampRole role) { champRoles.save(role); }
    public void saveTeamRole(TeamRole role) { teamRoles.save(role); }
    public void savePlayerRole(PlayerRole role) { playerRoles.save(role); }

//    public Champ getChampByIdOld(Long id) {
//        Optional<Users> users = usersRepository.findById(id);
//        ArrayList<Users> arrayList = new ArrayList<>();
//        users.ifPresent(arrayList::add);
//        model.addAttribute("champ", arrayList);
//        return repChamps.findById(id).get();
//    }
}
