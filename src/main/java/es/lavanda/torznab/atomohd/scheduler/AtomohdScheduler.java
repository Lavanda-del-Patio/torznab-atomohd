package es.lavanda.torznab.atomohd.scheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.lavanda.torznab.atomohd.service.AtomoHDService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AtomohdScheduler {

  private final AtomoHDService atomoHDServiceImpl;

  @Scheduled(fixedDelay = 180000)
  public void updateAtomohd() {
    log.info("Updating Atomohd");
    atomoHDServiceImpl.updateInternalData();
    log.info("Updated atomohd");
  }

}
