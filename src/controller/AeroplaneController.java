package controller;

import exception.InvalidPlaneException;
import exception.ResourceNotFoundException;
import model.Aeroplane;
import io.FileManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AeroplaneController {

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
