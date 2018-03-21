package model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "Crews")
public class CrewWrapper {
	private List<FireFighterCrew> crew;
	
	@XmlElement(name = "crew")
	public List<FireFighterCrew> getCrews(){
		return crew;
	}
	
	public void setCrews(List <FireFighterCrew> crew) {
		this.crew = crew;
	}
}