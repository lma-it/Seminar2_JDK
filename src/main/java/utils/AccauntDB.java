package utils;

import java.util.Random;

public class AccauntDB {


    private
    static final String[] names = {
            "Adam", "Michael", "Lola", "Anna", "Violetta", "Lion", "Freddy", "Lev", "Dmitriy",
            "Sergey", "Svetlana", "Naomi", "Alex", "Oleg", "Pavel", "Smith", "Omar", "Vlad",
            "Vadim", "Vyacheslav", "Inna", "Maria", "Sara", "Ann", "Chu", "Pu", "Mila",
            "Yakov", "Sam", "Masha", "Olga", "Ola", "Viola", "Vera", "Sonya", "Bella"
    };

    public static String getRandomName(){
        return names[new Random().nextInt(0, AccauntDB.names.length)];
    }

    public static String getRandomPassword(){
        int passwordSize = 8;
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < passwordSize; i++){
            password.append(new Random().nextInt(10));
        }
        return password.toString();
    }
}
