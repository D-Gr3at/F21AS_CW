package controller;

import exception.InvalidPlaneException;
import exception.ResourceNotFoundException;
import model.Aeroplane;
import io.FileManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/*
* This controller handles requests related to aeroplanes
*/
public class AeroplaneController {

    /*
    * This method gets a plane given the planeCode
    * @throws IOException
    * @throws ResourceNotFoundException, InvalidPlaneException
    * */
    public Aeroplane getPlaneByCode(String code) throws IOException, ResourceNotFoundException, InvalidPlaneException {
        Optional<Aeroplane> optionalAeroplane = FileManager.loadAeroplanes()
                .stream()
                .filter(aeroplane -> aeroplane.getModel().equalsIgnoreCase(code))
                .findFirst();
        if (!optionalAeroplane.isPresent()) {
            throw new ResourceNotFoundException("Plane not found");
        }
        return optionalAeroplane.get();
    }

    /*
     * This method gets all aeroplanes from FileManger
     * @throws IOException
     * @throws InvalidPlaneException
     * */
    public String[] getPlaneCodes() throws InvalidPlaneException {
        String[] planeCodes = null;
        try {
            List<String> planeModels = FileManager.loadAeroplanes()
                    .stream()
                    .map(Aeroplane::getModel)
                    .collect(Collectors.toList());
            planeCodes = new String[planeModels.size()];
            planeModels.toArray(planeCodes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return planeCodes;
    }
}
