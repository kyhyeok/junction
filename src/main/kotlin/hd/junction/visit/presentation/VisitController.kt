package hd.junction.visit.presentation

import hd.junction.visit.application.VisitService
import hd.junction.visit.dto.request.VisitCreateRequestDto
import hd.junction.visit.dto.request.VisitUpdateRequestDto
import hd.junction.visit.dto.response.VisitResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/visits")
class VisitController(
    private val visitService: VisitService,
) {
    @PostMapping()
    fun createVisit(
        @RequestBody VisitCreateRequestDto: VisitCreateRequestDto
    ): ResponseEntity<VisitResponseDto> {
        val response = visitService.createVisit(VisitCreateRequestDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PatchMapping("/{id}")
    fun updateVisit(
        @PathVariable("id", required = true) id: Long,
        @RequestBody visitUpdateRequestDto: VisitUpdateRequestDto
    ): ResponseEntity<VisitResponseDto> {
        val response = visitService.updateVisit(id, visitUpdateRequestDto)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteVisit(
        @PathVariable("id", required = true) id: Long
    ): ResponseEntity<Unit> {
        visitService.deleteVisit(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    fun getVisitDetail(
        @PathVariable("id", required = true) id: Long
    ): ResponseEntity<VisitResponseDto> {
        val response = visitService.getVisitDetail(id)
        return ResponseEntity.ok(response)
    }
}