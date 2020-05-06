package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockPTS;
  private TorpedoStore mockSTS;

  @BeforeEach
  public void init(){
    mockPTS = mock(TorpedoStore.class);
    mockSTS = mock(TorpedoStore.class);
    this.ship = new GT4500(mockPTS, mockSTS);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(mockPTS.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(mockPTS, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(mockPTS.fire(1)).thenReturn(true);
    when(mockSTS.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(mockPTS, times(1)).fire(1);
    verify(mockSTS, times(1)).fire(1);
  }

  @Test
  public void primaryFiredLast_Secondary_Success(){
    // Arrange
    when(mockPTS.fire(1)).thenReturn(true);
    when(mockSTS.fire(1)).thenReturn(true);
    // Act
    ship.fireTorpedo(FiringMode.SINGLE);
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(mockPTS, times(1)).fire(1);
    verify(mockSTS, times(1)).fire(1);
  }

  @Test
  public void primaryEmpty_Secondary_Success(){
     // Arrange
     when(mockPTS.isEmpty()).thenReturn(true);
     when(mockSTS.fire(1)).thenReturn(true);
     // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(mockSTS, times(1)).fire(1);
  }

  @Test
  public void secondaryEmpty_Primary_Success(){
    // Arrange
     when(mockSTS.isEmpty()).thenReturn(true);
     when(mockPTS.fire(1)).thenReturn(true);
     // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(mockPTS, times(1)).fire(1);
  }

  @Test
  public void bothEmpty_Single_Failure(){
    // Arrange
    when(mockSTS.isEmpty()).thenReturn(true);
    when(mockPTS.isEmpty()).thenReturn(true);
    // Act
   boolean result = ship.fireTorpedo(FiringMode.SINGLE);

   // Assert
   assertEquals(false, result);
  }

  @Test
  public void bothEmpty_All_Failure(){
    // Arrange
    when(mockSTS.isEmpty()).thenReturn(true);
    when(mockPTS.isEmpty()).thenReturn(true);
    // Act
   boolean result = ship.fireTorpedo(FiringMode.ALL);

   // Assert
   assertEquals(false, result);
  }

  @Test
  public void bothRunOut_Failure(){
    // Arrange
    when(mockPTS.fire(1)).thenReturn(true);
    when(mockSTS.fire(1)).thenReturn(true);
    // Act
    boolean firstShot = ship.fireTorpedo(FiringMode.SINGLE);

    when(mockPTS.isEmpty()).thenReturn(true);

    boolean secondShot = ship.fireTorpedo(FiringMode.SINGLE);

    when(mockSTS.isEmpty()).thenReturn(true);

    boolean thirdShot = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, firstShot);
    assertEquals(true, secondShot);
    assertEquals(false, thirdShot);
    
    verify(mockPTS, times(1)).fire(1);
    verify(mockSTS, times(1)).fire(1);
  }

  @Test
  public void SecondaryEmpty_FirePrimaryTwice_Success(){
    // Arrange
    when(mockSTS.isEmpty()).thenReturn(true);
    when(mockPTS.fire(1)).thenReturn(true);
    // Act
    ship.fireTorpedo(FiringMode.SINGLE);
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(mockPTS, times(2)).fire(1);
  }

  @Test
  public void Primary_Failure_All(){
    // Arrange
    when(mockPTS.fire(1)).thenReturn(false);
    when(mockSTS.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void Secondary_Failure_All(){
    // Arrange
    when(mockPTS.fire(1)).thenReturn(true);
    when(mockSTS.fire(1)).thenReturn(false);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void SecondaryEmpty_Failure_All(){
    // Arrange
    when(mockSTS.isEmpty()).thenReturn(true);
    when(mockPTS.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void SecondaryEmpty_PrimaryRunsOut_Failure(){
    // Arrange
    when(mockPTS.fire(1)).thenReturn(true);
    when(mockSTS.isEmpty()).thenReturn(true);
    // Act
    boolean firstShot = ship.fireTorpedo(FiringMode.SINGLE);

    when(mockPTS.isEmpty()).thenReturn(true);

    boolean secondShot = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, firstShot);
    assertEquals(false, secondShot);
    
    verify(mockPTS, times(1)).fire(1);
  }
}
