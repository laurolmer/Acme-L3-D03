
package acme.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Statistic {

	public Statistic() {

	}


	// Serialisation identifier -----------------------------------------------
	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	int							count;

	Double						average;

	Double						minimum;

	Double						maximum;

	Double						deviation;

}
