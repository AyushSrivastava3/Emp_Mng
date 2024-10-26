package com.employee_management_system.Employee_Management.Service;

import com.employee_management_system.Employee_Management.Model.Client;
import com.employee_management_system.Employee_Management.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getClientsCreatedToday() {
        LocalDate today = LocalDate.now();  // Use LocalDate for today's date

        // Call the repository method with the correct type
        return clientRepository.findByCreatedDate(today);
    }



    public List<Client> getClientsCreatedInWeek() {
        // Get the current date
        LocalDate today = LocalDate.now();

        // Calculate the start of a week ago
        LocalDate oneWeekAgo = today.minusDays(7);

        // Fetch clients created between one week ago and today
        return clientRepository.findByCreatedDateBetween(oneWeekAgo, today);
    }

    public void deleteClientById(Integer id){
        if(clientRepository.existsById(id)){
            clientRepository.deleteById(id);
        }else {
            // throw new ClientNotFoundException("Client with id " + id + " not found");
        }
    }
}
