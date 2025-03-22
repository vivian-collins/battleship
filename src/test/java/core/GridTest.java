package core;

import static core.Ship.Direction.HORIZONTAL;
import static core.Ship.Direction.VERTICAL;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class GridTest {

    Grid testGrid = new Grid(5, 5, DefaultGridBuilder.defaultShipsFor5x5());

    @Test
    public void aNewGridHasProvidedDimensions() {
        assertEquals(testGrid.numRows(), 5);
        assertEquals(testGrid.numCols(), 5);
    }

    @Test
    public void aNewGridHasNoneNullCells() {
        for (int i = 1; i <= testGrid.numRows(); i++) {
            for (int j = 1; j <= testGrid.numCols(); j++) {
                assertNotEquals(null, testGrid.getStatus(new Coord(i, j)));
            }
        }
    }

    @Test
    public void aNewGridCorrectlyMarksShips() {
        assertEquals(CellStatus.ShipUnrevealed, testGrid.getStatus(new Coord(1, 2)));
        assertEquals(CellStatus.ShipUnrevealed, testGrid.getStatus(new Coord(2, 2)));
        assertEquals(CellStatus.ShipUnrevealed, testGrid.getStatus(new Coord(3, 2)));
        assertEquals(CellStatus.ShipUnrevealed, testGrid.getStatus(new Coord(5, 2)));
        assertEquals(CellStatus.Unknown, testGrid.getStatus(new Coord(4, 2)));
        assertEquals(CellStatus.Unknown, testGrid.getStatus(new Coord(1, 1)));
    }

    @Test
    public void addShipUpdatesShipListCorrectly() {
        Ship ship1 = new Ship(new Coord(1, 2), 3, VERTICAL, "Submarine");
        Ship ship2 = new Ship(new Coord(5, 1), 5, HORIZONTAL, "Carrier");
        Ship ship3 = new Ship(new Coord(1, 5), 3, VERTICAL, "Destroyer");
        Ship addedShip = new Ship(new Coord(4, 5), 1, VERTICAL, "Ship");
        testGrid.addShip(addedShip);
        assertTrue(assertEqualShips(ship1, testGrid.getShipList().get(0)));
        assertTrue(assertEqualShips(ship2, testGrid.getShipList().get(1)));
        assertTrue(assertEqualShips(ship3, testGrid.getShipList().get(2)));
        assertTrue(assertEqualShips(addedShip, testGrid.getShipList().get(3)));
    }

    public static boolean assertEqualShips(Ship expected, Ship actual) {
        if (expected.getSize() != actual.getSize()) {
            return false;
        }
        for (int i = 0; i < expected.getSize(); i++) {
            if (!expected.getCoordList().get(i).isEqual(actual.getCoordList().get(i))) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void whenShootingAndNotAlreadyShotChangeCellToShot() {
        testGrid.shoot(new Coord(2, 2));
        CellStatus shootCellStatus = testGrid.getStatus(new Coord(2, 2));
        assertEquals(shootCellStatus, CellStatus.ShipHit);
    }

    @Test
    public void allShipsHaveNotBeenShot() {
        boolean result = testGrid.allShipsAreSunk();
        assertFalse(result);
    }

    @Test
    public void allShipsArePartiallyShot() {
        testGrid.shoot(new Coord(5, 1));
        testGrid.shoot(new Coord(1, 5));
        boolean result = testGrid.allShipsAreSunk();
        assertFalse(result);
    }

    @Test
    public void allShipsAreShot() {
        List<Ship> shipList = testGrid.getShipList();
        for (Ship ship : shipList) {
            List<Coord> coords = ship.getCoordList();
            for (Coord coord : coords) {
                testGrid.shoot(coord);
                testGrid.shoot(coord);
            }
        }
        boolean result = testGrid.allShipsAreSunk();
        assertTrue(result);
    }

    @Test
    public void isShipOnGridReturnsTrueWhenShipIsShipOnGrid() {
        List<Ship> ships = testGrid.getShipList();
        assertTrue(testGrid.isShipOnGrid(ships.get(0)));
    }

    @Test
    public void isShipOnGridReturnsFalseWhenShipIsShipOnGrid() {
        Coord c1 = new Coord(1, 2);
        Ship otherShip = new Ship(c1, 6, VERTICAL, "BattleShip");
        assertFalse(testGrid.isShipOnGrid(otherShip));
    }

    @Test
    public void isShipSunkReturnsTrueIfAllCellsMarkedAsHit() {
        Ship ship = new Ship(new Coord(1, 2), 3, VERTICAL, "BattleShip");
        Coord c1 = new Coord(1, 2);
        Coord c2 = new Coord(2, 2);
        Coord c3 = new Coord(3, 2);
        testGrid.shoot(c1);
        testGrid.shoot(c2);
        assertFalse(testGrid.isShipSunk(ship));
        testGrid.shoot(c3);
        assertTrue(testGrid.isShipSunk(ship));
    }
}
