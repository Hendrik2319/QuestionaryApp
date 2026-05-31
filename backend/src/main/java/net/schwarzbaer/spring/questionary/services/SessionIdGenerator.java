package net.schwarzbaer.spring.questionary.services;

import java.util.Random;
import java.util.stream.Collectors;

public class SessionIdGenerator
{
    private final int length;
    private final Random random;

    SessionIdGenerator(int length)
    {
        this.length = length;
        random = new Random();
    }

    String generate()
    {
        return random
            .ints(length, ((int)'A'), ((int)'Z')+1)
            .mapToObj(n -> Character.toString((char)n))
            .collect(Collectors.joining());
    }
}
