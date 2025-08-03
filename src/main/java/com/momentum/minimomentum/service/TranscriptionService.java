package com.momentum.minimomentum.service;

import com.momentum.minimomentum.constant.PromptConstants;
import com.momentum.minimomentum.dto.responseDTO.TranscriptResponseDTO;
import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.model.Transcript;
import com.momentum.minimomentum.repository.TranscriptionRepository;
import com.momentum.minimomentum.service.openAiService.OpenAiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
 * TranscriptionService is responsible for generating and managing transcripts.
 * It interacts with the OpenAI API to generate transcripts based on the provided language
 * and provides methods to retrieve, save, and convert transcripts to response DTOs.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TranscriptionService {

    private final OpenAiClient openAiClient;

    private final TranscriptionRepository transcriptRepository;

    private final String className = getClass().getSimpleName();

    /**
     * Generates a transcript based on the provided language. If no language is
     * provided, it defaults to English.
     *
     * @param language the language for the transcript (default is "English")
     * @return a TranscriptResponseDTO containing the generated transcript
     */
    public TranscriptResponseDTO generateTranscript(String language) {
        String transcriptPrompt = PromptConstants.GENERATION_PROMPT_CONSTANT
                .replace("%s", language)
                .replaceAll("\\s+", " ").trim();

        String content = openAiClient.getCompletionOpenAi(transcriptPrompt);

        Transcript transcript = createAndSaveTranscripts(content, language);
        log.info("[{}] Generated transcript for language: {}", className, language);
        return toTranscriptResponseDTO(transcript);
    }

    /**
     * Creates and saves a transcript with the given content and language.
     *
     * @param content the content of the transcript
     * @param language the language of the transcript
     * @return the saved Transcript object
     */
    public Transcript createAndSaveTranscripts(String content, String language) {
        Transcript transcript = new Transcript();
        transcript.setLanguage(language);
        transcript.setTranscriptText(content);
        transcript.setCreateDateTime(LocalDateTime.now());
        log.info("[{}] Creating and saving transcript with language: {}", className, language);
        return transcriptRepository.save(transcript);
    }

    /**
     * Retrieves a transcript by its ID. If the ID is not found in the database,
     * it throws an EntityNotFoundException.
     *
     * @param id the ID of the transcript to retrieve
     * @return the Transcript object if found
     */
    public Transcript getTranscriptById(Long id) {
        log.info("[{}] Fetching transcript by id: {}", className, id);
        return transcriptRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Transcript not found by id: " + id));
    }

    /**
     * Retrieves a transcript by its ID and converts it to a
     * TranscriptResponseDTO. If the ID is not found in the database, it throws
     * an EntityNotFoundException.
     *
     * @param id the ID of the transcript to retrieve
     * @return a TranscriptResponseDTO containing the transcript details
     */

    public TranscriptResponseDTO getTranscriptDtoById(Long id) {
        Transcript transcript = getTranscriptById(id);
        return toTranscriptResponseDTO(transcript);
    }

    /**
     * Retrieves all transcripts from the database. If no transcripts are found,
     * it throws an EntityNotFoundException.
     *
     * @return a list of TranscriptResponseDTO containing all transcripts
     */

    public List<TranscriptResponseDTO> getAllTranscripts() {
        List<Transcript> transcriptList = transcriptRepository.findAll();

        if (transcriptList.isEmpty()) {
            throw new EntityNotFoundException("No transcripts found.");
        }
        log.info("[{}] Fetched {} transcripts", className, transcriptList.size());
        return transcriptList.stream().map(this::toTranscriptResponseDTO).collect(Collectors.toList());
    }

    /**
     * Converts a Transcript entity to a TranscriptResponseDTO.
     *
     * @param transcript the Transcript entity to convert
     * @return a TranscriptResponseDTO containing the transcript details
     */

    public TranscriptResponseDTO toTranscriptResponseDTO(Transcript transcript) {
        TranscriptResponseDTO responseDto = new TranscriptResponseDTO();
        responseDto.setTranscriptId(transcript.getId());
        responseDto.setTranscriptText(transcript.getTranscriptText());
        responseDto.setLanguage(transcript.getLanguage());
        responseDto.setCreateDateTime(transcript.getCreateDateTime());
        return responseDto;
    }
}
