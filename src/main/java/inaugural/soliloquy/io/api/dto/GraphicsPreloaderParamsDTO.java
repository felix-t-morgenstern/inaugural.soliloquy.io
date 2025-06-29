package inaugural.soliloquy.io.api.dto;

import java.util.List;
import java.util.Map;

public class GraphicsPreloaderParamsDTO {
    public Map<String, Boolean> ImageDirectoriesWithMouseEventCapturingSupport;
    public String FontsDirectory;
    public Map<String, Integer> PreloadingComponentBatchSizes;
    public List<String> SupportedImageFiletypes;
    public float ParallelAssetsToLoadPerSecondTarget;
}
