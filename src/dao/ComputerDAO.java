package dao;

import enums.ComputerStatus;
import enums.ComputerType;
import models.computer.Computer;

import java.util.List;

public interface ComputerDAO {
    String getLastComputerId();
    void insert(Computer computer);
    Computer findById(String computerId);
    List<Computer> findAll();
    List<Computer> findAvailable();
    void updateStatus(String computerId, ComputerStatus status);
    void updatePrice(String computerId, double price);
    void updateType(String computerId, ComputerType type);
    void updateName(String computerId, String name);
    boolean delete(String computerId);
}
