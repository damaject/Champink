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

    @Autowired private SportRepository sports;
    @Autowired private ChampRepository champs;
    @Autowired private ChampTeamRepository champTeams;
    @Autowired private ChampEventRepository champEvents;
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

    public List<Player> getUserPlayersNotInTeam(User user, Team team) { return players.findPlayersByUserRoleAndNotInTeam(team, user); }
    public List<Team> getUserTeamsNotInChamp(User user, Champ champ) { return teams.findTeamsByUserRoleAndNotInChamp(champ, user); }

    public List<Champ> getGlobalChamps() { return champs.findChampsByPrivatIsFalse(sortDescId); }
    public List<Champ> getUserChampsAll(User user) { return champs.findChampsByUserAll(user); }
    public List<Champ> getUserChampsRole(User user, int role) { return champs.findChampsByUserRole(user, role); }

    public List<Team> getGlobalTeams() { return teams.findTeamsByPrivatIsFalse(sortDescId); }
    public List<Team> getUserTeamsAll(User user) { return teams.findTeamsByUserAll(user); }
    public List<Team> getUserTeamsRole(User user, int role) { return teams.findTeamsByUserRole(user, role); }

    public List<Player> getGlobalPlayers() { return players.findPlayersByPrivatIsFalse(sortDescId); }
    public List<Player> getUserPlayersAll(User user) { return players.findPlayersByUserAll(user); }
    public List<Player> getUserPlayersRole(User user, int role) { return players.findPlayersByUserRole(user, role); }

    public User getUserById(Long id) { return users.findById(id).orElse(null); }
    public Sport getSportById(Long id) { return sports.findById(id).orElse(null); }
    public Champ getChampById(Long id) { return champs.findById(id).orElse(null); }
    public ChampRole getChampRoleById(Long id) { return champRoles.findById(id).orElse(null); }
    public ChampTeam getChampTeamById(Long id) { return champTeams.findById(id).orElse(null); }
    public ChampEvent getChampEventById(Long id) { return champEvents.findById(id).orElse(null); }
    public Team getTeamById(Long id) { return teams.findById(id).orElse(null); }
    public TeamRole getTeamRoleById(Long id) { return teamRoles.findById(id).orElse(null); }
    public TeamPlayer getTeamPlayerById(Long id) { return teamPlayers.findById(id).orElse(null); }
    public Player getPlayerById(Long id) { return players.findById(id).orElse(null); }
    public PlayerRole getPlayerRoleById(Long id) { return playerRoles.findById(id).orElse(null); }

    public User getUserByUsername(String username) { return users.findByUsername(username); }

    public void addNewUser(User user) { saveUser(user); }
    public void addNewChamp(Champ champ) { champs.save(champ); }
    public void addNewChampTeam(ChampTeam champTeam) { champTeams.save(champTeam); }
    public void addNewChampRole(ChampRole role) { saveChampRole(role);}
    public void addNewChampEvent(ChampEvent event) { champEvents.save(event); }
    public void addNewTeam(Team team) { teams.save(team); }
    public void addNewTeamPlayer(TeamPlayer teamPlayer) { teamPlayers.save(teamPlayer); }
    public void addNewTeamRole(TeamRole role) { saveTeamRole(role); }
    public void addNewPlayer(Player player) { players.save(player); }
    public void addNewPlayerRole(PlayerRole role) { savePlayerRole(role); }

    public void saveUser(User user) { users.save(user); }
    public void saveChamp(Champ champ) { champs.save(champ); }
    public void saveTeam(Team team) { teams.save(team); }
    public void savePlayer(Player player) { players.save(player); }
    public void saveChampRole(ChampRole role) { champRoles.save(role); }
    public void saveTeamRole(TeamRole role) { teamRoles.save(role); }
    public void savePlayerRole(PlayerRole role) { playerRoles.save(role); }
    public void saveChampEvent(ChampEvent champEvent) { champEvents.save(champEvent); }

    public void deleteUser(User user) { users.delete(user); }
    public void deleteChamp(Champ champ) { champs.delete(champ); }
    public void deleteTeam(Team team) { teams.delete(team); }
    public void deletePlayer(Player player) { players.delete(player); }
    public void deleteTeamPlayer(TeamPlayer teamPlayer) { teamPlayers.delete(teamPlayer); }
    public void deleteChampTeam(ChampTeam champTeam) { champTeams.delete(champTeam); }
    public void deleteChampEvent(ChampEvent champEvent) { champEvents.delete(champEvent); }
}
