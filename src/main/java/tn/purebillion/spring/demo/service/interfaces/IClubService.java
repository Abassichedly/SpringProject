package tn.purebillion.spring.demo.service.interfaces;

import tn.purebillion.spring.demo.entity.Club;

import java.util.List;

public interface IClubService {
    List<Club> retrieveAllClubs();
    Club addClub(Club club);
    Club updateClub(Club club);
    Club retrieveClub(Long id);
    void removeClub(Long id);
}