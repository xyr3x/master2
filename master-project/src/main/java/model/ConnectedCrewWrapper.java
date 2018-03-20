package model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "connectedCrews")
public class ConnectedCrewWrapper {
	private List<ConnectedFireFighterCrew> crew;
	
	@XmlElement(name = "crew")
	public List<ConnectedFireFighterCrew> getCrews(){
		return crew;
	}
	
	public void setCrews(List <ConnectedFireFighterCrew> crew) {
		this.crew = crew;
	}
}
