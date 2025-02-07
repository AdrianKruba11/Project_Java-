package com.example.biblioteka.repository;

import com.example.biblioteka.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    // Znajdź wypożyczenie na podstawie książki i użytkownika
    @Query("SELECT r FROM Rental r WHERE r.book.id = :bookId AND r.user.id = :userId")
    Optional<Rental> findByBookIdAndUserId(@Param("bookId") Long bookId, @Param("userId") Long userId);

    // Znajdź wszystkie wypożyczenia danej książki
    @Query("SELECT r FROM Rental r WHERE r.book.id = :bookId")
    Optional<Rental> findByBookId(@Param("bookId") Long bookId);

    // Znajdź aktywne wypożyczenie (niezwrócone) dla książki i użytkownika
    @Query("SELECT r FROM Rental r WHERE r.book.id = :bookId AND r.user.id = :userId AND r.returnAt IS NULL")
    Optional<Rental> findActiveRental(@Param("bookId") Long bookId, @Param("userId") Long userId);

    // Znajdź wszystkie wypożyczenia użytkownika
    @Query("SELECT r FROM Rental r WHERE r.user.id = :userId")
    List<Rental> findAllByUserId(@Param("userId") Long userId);

    // Znajdź aktywne wypożyczenia użytkownika
    @Query("SELECT r FROM Rental r WHERE r.user.id = :userId AND r.returnAt IS NULL")
    List<Rental> findActiveRentalsByUserId(@Param("userId") Long userId);

    // Znajdź aktywne wypożyczenia danej książki
    @Query("SELECT r FROM Rental r WHERE r.book.id = :bookId AND r.returnAt IS NULL")
    List<Rental> findActiveRentalsByBookId(@Param("bookId") Long bookId);

    @Query("SELECT r FROM Rental r WHERE r.user.id = :userId AND r.returnAt IS NOT NULL")
    List<Rental> findReturnedBooksByUserId(@Param("userId") Long userId);


    @Query("SELECT r FROM Rental r WHERE r.user.id = :userId AND r.returnAt IS NULL")
    List<Rental> findActiveRentalsByUser(@Param("userId") Long userId);



}
