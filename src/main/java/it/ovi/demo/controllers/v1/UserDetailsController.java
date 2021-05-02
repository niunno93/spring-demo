package it.ovi.demo.controllers.v1;

import it.ovi.demo.controllers.v1.dto.CreateUserDetailDto;
import it.ovi.demo.controllers.v1.dto.UserDetailDto;
import it.ovi.demo.errors.InvalidRequestException;
import it.ovi.demo.models.UserDetail;
import it.ovi.demo.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/v1/user-details")
public class UserDetailsController {

    private final UserService userService;

    public UserDetailsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Page<UserDetailDto> get(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userService.findAll(pageRequest).map(UserDetailsController::map);
    }

    @GetMapping("{id}")
    public UserDetailDto get(@PathVariable long id) {
        return map(userService.findById(id).orElseThrow());
    }

    @PostMapping
    public UserDetailDto post(@RequestBody @Valid CreateUserDetailDto userDetail) {
        return map(userService.create(map(userDetail)));
    }

    @PutMapping("{id}")
    public UserDetailDto put(
            @PathVariable long id,
            @RequestBody @Valid @NotNull UserDetailDto dto
    ) {
        if (id != dto.getId()) {
            throw new InvalidRequestException(String.format("Id is %s but user id is %s; they must be the same", id, dto.getId()));
        }

        return map(userService.update(map(dto)));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable long id) {
        userService.deleteById(id);
    }

    private static UserDetailDto map(UserDetail model) {
        return new UserDetailDto(model.getId(), model.getName(), model.getEmail(), model.getCreationDate(),
                model.getModificationDate());
    }

    private static UserDetail map(CreateUserDetailDto dto) {
        return new UserDetail(null, dto.getName(), dto.getEmail(), null, null);
    }

    private static UserDetail map(UserDetailDto dto) {
        return new UserDetail(dto.getId(), dto.getName(), dto.getEmail(), dto.getCreationDate(),
                dto.getModificationDate());
    }
}
