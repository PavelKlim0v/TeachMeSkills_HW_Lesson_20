package com.teachmeskills.lesson_20.task_1.crud;

import com.teachmeskills.lesson_20.task_1.connector.MySQLConnector;
import com.teachmeskills.lesson_20.task_1.model.Location;
import com.teachmeskills.lesson_20.task_1.model.Student;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class CRUD {

    private static final String INSERT_STUDENT = "INSERT INTO student (name, surname) VALUES (?, ?)";
    private static final String INSERT_LOCATION = "INSERT INTO location (location) VALUES (?)";
    private static final String GET_STUDENT = "SELECT * FROM student";
    private static final String GET_LOCATION = "SELECT * FROM location";
    private static final String DELETE_STUDENT = "DELETE FROM student WHERE id = ?";
    private static final String DELETE_LOCATION = "DELETE FROM location WHERE id = ?";
    private static Student student = null;
    private static Location location = null;
    private PreparedStatement preparedStatement = null;
    private MySQLConnector connector1 = null;
    private MySQLConnector connector2 = null;
    private Scanner scanner = new Scanner(System.in);

    // добавить студента, нужно ввести: имя, фамилию (а также будет вызван метод по добавлению города проживания)(и ввести данные студента)
    public boolean createStudent() {
        createInnerMethod("student");
        createInnerMethod("location");
        return true;
    }

    // добавить место проживания студента, нужно ввести: город проживания
    // (а также будет вызван метод по добавлению данных студента)(и ввести данные студента)
    public boolean createLocation() {
        createInnerMethod("location");
        createInnerMethod("student");
        return true;
    }

    // удалить студента по id (это номер под которым был добавлен в бд), а также удалится город проживания студента
    public boolean deleteStudent() {
        deleteInnerMethod("student");
        return true;
    }

    // удалить город проживания по id (это номер под которым был добавлен в бд), а также удалятся данные студента
    public boolean deleteLocation() {
        deleteInnerMethod("location");
        return true;
    }

    private void createInnerMethod(String data) {
        try {
            connector1 = new MySQLConnector();
            if(data.equals("student")) {
                System.out.print("Укажите данные для добавления студента: ");
                preparedStatement = connector1.getConnection(1).prepareStatement(INSERT_STUDENT);

                // здесь добавляются данные студента в бд, через консоль
                int x = 1;
                while (x < 3) {
                    preparedStatement.setString(x, scanner.next());
                    x++;
                }

            } else if(data.equals("location")) {
                preparedStatement = connector1.getConnection(2).prepareStatement(INSERT_LOCATION);
                System.out.print("Укажите данные для добавления города проживания студента: ");

                // здесь добавляются данные по месту проживания студента в бд, через консоль
                preparedStatement.setString(1, scanner.next());
            }
            preparedStatement.executeUpdate();

        } catch (IOException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void deleteInnerMethod(String data) {
        try {
            connector1 = new MySQLConnector();
            connector2 = new MySQLConnector();

            if(data.equals("student")) {
                System.out.print("Укажите индекс для удаления данных студента: ");
                preparedStatement = connector1.getConnection(1).prepareStatement(DELETE_STUDENT);

            } else if(data.equals("location")) {
                System.out.print("Укажите индекс для удаления города проживания студента: ");
                preparedStatement = connector2.getConnection(2).prepareStatement(DELETE_LOCATION);
            }
            int id = scanner.nextInt();
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            if(data.equals("student")) {
                preparedStatement = connector2.getConnection(2).prepareStatement(DELETE_LOCATION);

            } else if(data.equals("location")) {
                preparedStatement = connector1.getConnection(1).prepareStatement(DELETE_STUDENT);
            }
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (IOException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    // вывести в консоль список всех студентов(все данные студентов)
    public boolean getAllStudents() {
        try {
            connector1 = new MySQLConnector();
            connector2 = new MySQLConnector();
            Statement statement1 = connector1.getConnection(1).createStatement();
            Statement statement2 = connector2.getConnection(2).createStatement();
            ResultSet resultSet1 = statement1.executeQuery(GET_STUDENT);
            ResultSet resultSet2 = statement2.executeQuery(GET_LOCATION);

            System.out.println("Список всех студентов:");
            while (resultSet1.next() && (resultSet2.next())) {
                student = new Student();
                student.setIdStudent(resultSet1.getInt("id"));
                student.setName(resultSet1.getString("name"));
                student.setSurname(resultSet1.getString("surname"));

                location = new Location();
                location.setIdLocation(resultSet2.getInt("id"));
                location.setLocation(resultSet2.getString("location"));

                System.out.println(customToString());
            }
            scanner.close();    // закрытие потока сканнера
            return true;

        } catch (IOException | SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private String customToString() {
        return "Student {" +
                " id: " + student.getIdStudent() +
                ", name: " + student.getName() +
                ", surname: " + student.getSurname() +
                ", location: " + location.getLocation() +
                " }";
    }

}
