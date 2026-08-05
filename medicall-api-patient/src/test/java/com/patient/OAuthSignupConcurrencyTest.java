package com.patient;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.medicall.domain.patient.NewPatient;
import com.medicall.domain.patient.Patient;
import com.medicall.domain.patient.PatientService;

@SpringBootTest
class OAuthSignupConcurrencyTest {

    @Autowired
    private PatientService patientService;

    @Test
    void 같은_소셜_계정으로_동시에_가입해도_한_명만_생성된다() throws Exception {
        NewPatient newPatient = new NewPatient("동시가입", null, "concurrent@test.local", "concurrent-oauth-1", "kakao");

        int threads = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<Callable<Patient>> tasks = java.util.Collections.nCopies(
                threads, () -> patientService.findOrCreateByOAuth(newPatient));

        List<Future<Patient>> results = executor.invokeAll(tasks);
        executor.shutdown();

        List<Long> ids = results.stream().map(future -> {
            try {
                return future.get().id();
            } catch (Exception e) {
                throw new IllegalStateException("가입 실패", e);
            }
        }).distinct().toList();

        // 모든 요청이 성공하고, 전부 같은 환자를 가리켜야 한다
        assertThat(ids).hasSize(1);
    }
}
