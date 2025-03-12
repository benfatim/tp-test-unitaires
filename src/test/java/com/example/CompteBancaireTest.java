package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompteBancaireTest {
    private CompteBancaire compte;
    private NotificationService notificationServiceMock;

    @BeforeEach
    void setUp() {
        notificationServiceMock = mock(NotificationService.class);
        compte = new CompteBancaire(100, notificationServiceMock);
    }

    @Test
    void testDepot() {
        compte.deposer(50);
        assertEquals(150, compte.getSolde());

        verify(notificationServiceMock).envoyerNotification("Dépôt de 50.0 effectué.");
    }

    @Test
    void testRetrait() {
        compte.retirer(30);
        assertEquals(70, compte.getSolde());

        verify(notificationServiceMock).envoyerNotification("Retrait de 30.0 effectué.");
    }

    @Test
    void testRetraitSuperieurSolde() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            compte.retirer(200);
        });

        assertEquals("Fonds insuffisants.", exception.getMessage());

        verify(notificationServiceMock, never()).envoyerNotification(anyString());
    }

    @Test
    void testDepotMontantNegatif() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            compte.deposer(-10);
        });

        assertEquals("Le montant du dépôt doit être positif.", exception.getMessage());
    }
}
