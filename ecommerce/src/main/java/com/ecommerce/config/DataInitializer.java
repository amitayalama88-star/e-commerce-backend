package com.ecommerce.config;

import com.ecommerce.model.Item;
import com.ecommerce.model.User;
import com.ecommerce.repository.ItemRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, ItemRepository itemRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("Admin123").isEmpty()) {
            User admin = new User();
            admin.setUsername("Admin123");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }

        if (itemRepository.count() == 0) {
            itemRepository.save(new Item(null, "Item-1", "Basic gadget", 10.0, 100));
            itemRepository.save(new Item(null, "Item-2", "Standard accessory", 20.0, 90));
            itemRepository.save(new Item(null, "Item-3", "Mid-range device", 50.0, 80));
            itemRepository.save(new Item(null, "Item-4", "Advanced tool", 100.0, 70));
            itemRepository.save(new Item(null, "Item-5", "Premium item", 200.0, 60));
            itemRepository.save(new Item(null, "Item-6", "Luxury product", 500.0, 50));
            itemRepository.save(new Item(null, "Item-7", "Compact version", 75.0, 40));
            itemRepository.save(new Item(null, "Item-8", "Large version", 150.0, 30));
            itemRepository.save(new Item(null, "Item-9", "Special edition", 250.0, 20));
            itemRepository.save(new Item(null, "Item-10", "Limited release", 1000.0, 10));
        }
    }
}
