package core;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static core.Ship.Direction.VERTICAL;
import static org.junit.jupiter.api.Assertions.*;

class ShipListTest {
    Coord coordinate = new Coord(1, 2);
    Ship ship1 = new Ship(coordinate, 3, VERTICAL, "BattleShip1");
    Coord coordinate2 = new Coord(1, 2);
    Ship ship2 = new Ship(coordinate2, 3, VERTICAL, "BattleShip2");
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("test");

    @Test
    public void addShipListToDatabase(){
        ShipList shipListOriginal = new ShipList();
        shipListOriginal.addShip(ship1);
        shipListOriginal.addShip(ship2);
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(shipListOriginal);
        em.getTransaction().commit();
        em.close();

        long originalID = shipListOriginal.getId();

        EntityManager em2 = factory.createEntityManager();
        em2.getTransaction().begin();
        ShipList shipList2 = em2.find(ShipList.class, originalID);
        em2.close();

        assertEquals(shipListOriginal.getShips().size(), shipList2.getShips().size());
        assertEquals(shipListOriginal.getShips().get(0).getName(), shipList2.getShips().get(0).getName());
        assertEquals(shipListOriginal.getShips().get(1).getName(), shipList2.getShips().get(1).getName());
    }

    @Test
    public void addEmptyShipListToDatabase(){
        ShipList shipListOriginal = new ShipList();
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(shipListOriginal);
        em.getTransaction().commit();
        em.close();

        long originalID = shipListOriginal.getId();
        EntityManager em2 = factory.createEntityManager();
        em2.getTransaction().begin();
        ShipList shipList2 = em2.find(ShipList.class, originalID);
        em2.close();

        assertEquals(shipListOriginal.getShips().size(), shipList2.getShips().size());
        assertEquals(shipListOriginal.getShips().isEmpty(), shipList2.getShips().isEmpty());
    }



}