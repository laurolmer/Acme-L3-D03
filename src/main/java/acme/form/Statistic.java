
package acme.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistic {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							count;

	Double						average;

	Double						minimum;

	Double						maximum;

	Double						deviation;

}
