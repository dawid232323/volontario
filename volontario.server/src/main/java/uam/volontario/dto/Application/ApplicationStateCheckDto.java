package uam.volontario.dto.Application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationStateCheckDto
{
    private Long id;
    private boolean applied;
    private String state;
}
