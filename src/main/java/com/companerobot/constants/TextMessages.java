package com.companerobot.constants;

import com.companerobot.helpers.CyrillicStringHelper;

public class TextMessages {
    public static final String WELCOME_MESSAGE = """
            \uD83C\uDDEC\uD83C\uDDE7:<i>"Greetings! Before we begin, could you please tell me which language you prefer to speak?"</i>
            
            \uD83C\uDDEA\uD83C\uDDF8:<i>"Saludos. Antes de empezar, ¿podría decirme en qué idioma prefiere hablar?"</i>
            
            \uD83C\uDDFA\uD83C\uDDE6:<i>"%s"</i>
            
            \uD83C\uDDF5\uD83C\uDDF9:<i>"Saudações! Antes de começarmos, podem dizer-me qual a língua que preferem falar?"</i>
            
            \uD83C\uDDE9\uD83C\uDDEA:<i>"Seien Sie gegrüßt! Bevor wir beginnen, könnten Sie mir bitte sagen, welche Sprache Sie am liebsten sprechen?"</i>
            
            \uD83C\uDDEB\uD83C\uDDF7:<i>"Salutations ! Avant de commencer, pourriez-vous me dire quelle langue vous préférez parler ?"</i>
            """.formatted(
                    CyrillicStringHelper.getCyrillicString("Привітулі! Перед тим як почати, вкажи, будь ласка, якою мовою тобі зручніше спілкуватись?")
    );

    //USER
    public static final String USER_POLICY_AGREEMENT_MESSAGE = "user.policy.agreement.message";

    public static final String REQUEST_CONTACTS_MESSAGE = "request.contacts.message";

    public static final String MESSAGE_AFTER_REGISTRATION_WITH_PHONE_NUMBER = "message.after.registration.with.phone.number";

    public static final String MESSAGE_AFTER_REGISTRATION_WITHOUT_PHONE_NUMBER = "message.after.registration.without.phone.number";

    public static final String MESSAGE_MISSING_USER_TAG_GENERAL = "message.missing.user.tag.general";

    public static final String MISSING_USER_TAG_WITHOUT_NUMBER_PASSENGER_WARN_MESSAGE = "missing.user.tag.without.number.passenger.warn.message";

    public static final String MISSING_USER_TAG_WITHOUT_NUMBER_DRIVER_WARN_MESSAGE = "missing.user.tag.without.number.driver.warn.message";

    public static final String FINANCIAL_OPERATIONS_WARN_MESSAGE = "financial.operations.warn.message";

    public static final String INCORRECT_CONTACT_MESSAGE = "incorrect.contact.message";

    public static final String CHOOSE_ROLE_MESSAGE = "choose.role.message";

    public static final String AFTER_RATE_MESSAGE = "after.rate.message";

    public static final String ORDER_PRICE_MESSAGE = "order.price.message";

    public static final String ORDER_FREE_RIDE_MESSAGE = "order.free.ride.message";

    //PASSENGER MESSAGES
    public static final String PASSENGER_ROLE_WELCOME_MESSAGE = "passenger.role.welcome.message";

    public static final String REQUEST_PICKUP_POINT_PASSENGER_MESSAGE = "request.pickup.point.passenger.message";

    public static final String REQUEST_DESTINATION_POINT_PASSENGER_MESSAGE = "request.destination.point.passenger.message";

    public static final String ASK_IF_NOTES_NEEDED_MESSAGE = "ask.if.notes.needed.message";

    public static final String ADD_NOTES_MESSAGE = "add.notes.message";

    public static final String ADD_PRICE_ORDER_MESSAGE = "add.price.order.message";

    public static final String PRICE_ERROR_MORE_THAN_INT_MAX_MESSAGE = "price.error.more.than.int.max.message";

    public static final String PRICE_ERROR_LESS_THAN_0_MESSAGE = "price.error.less.than.0.message";

    public static final String PRICE_ERROR_OTHER_MESSAGE = "price.error.other.message";

    public static final String CONFIRMATION_PASSENGER_ORDER_BASE_MESSAGE = "confirmation.passenger.order.base.message";

    public static final String CONFIRMATION_REMINDER_MESSAGE = "confirmation.reminder.message";

    public static final String CONFIRM_ORDER_BY_PASSENGER_MESSAGE = "confirm.order.by.passenger.message";

    public static final String DRIVER_FOUND_WITHOUT_NUMBER_MESSAGE = "driver.found.without.number.message";

    public static final String DRIVER_FOUND_BASE_MESSAGE = "driver.found.base.message";

    public static final String DRIVER_FOUND_CAR_PLATE_MESSAGE = "driver.found.car.plate.message";

    public static final String DRIVER_FOUND_CAR_PLATE_POSTPONED_MESSAGE = "driver.found.car.plate.postponed.message";

    public static final String DRIVER_FOUND_PHONE_NUMBER_MESSAGE = "driver.found.phone.number.message";

    public static final String DRIVER_INFO_RATING_MESSAGE = "driver.info.rating.message";

    public static final String DRIVER_INFO_REVIEWS_AMOUNT_MESSAGE = "driver.info.reviews.amount.message";

    public static final String DRIVER_INFO_MISSING_CONTACTS_MESSAGE = "driver.info.missing.contacts.message";

    public static final String DRIVER_INFO_LOCATION_MESSAGE = "driver.info.location.message";

    public static final String DRIVER_INFO_FURTHER_CONTACTS_POSTPONED_MESSAGE = "driver.info.further.contacts.postponed.message";

    public static final String DRIVER_LOCATION_MESSAGE = "driver.location.message";

    public static final String DRIVER_FOUND_WITH_NUMBER_MESSAGE = "driver.found.with.number.message";

    public static final String FURTHER_STEPS_FOR_PASSENGER_POSTPONED_MESSAGE = "further.steps.for.passenger.postponed.message";

    public static final String DRIVER_FOUND_WITHOUT_TAG_AND_NUMBER_MESSAGE = "driver.found.without.tag.and.number.message";

    public static final String CANCEL_TRIP_BY_PASSENGER_SELF_MESSAGE = "cancel.trip.by.passenger.self.message";

    public static final String DRIVER_ARRIVED_PASSENGER_MESSAGE = "driver.arrived.passenger.message";

    public static final String FINISHED_ORDER_PASSENGER_MESSAGE = "finished.order.passenger.message";

    public static final String CANCEL_TRIP_BY_DRIVER_MESSAGE = "cancel.trip.by.driver.message";

    public static final String DECLINED_POSTPONED_TRIP_FOR_PASSENGER_MESSAGE = "declined.postponed.trip.for.passenger.message";

    public static final String CONFIRMED_POSTPONED_TRIP_FOR_PASSENGER_MESSAGE = "confirmed.postponed.trip.for.passenger.message";

    public static final String ADD_REVIEW_ON_DRIVER_FOR_PASSENGERS_MESSAGE = "add.review.on.driver.for.passengers.message";


    //DRIVER
    public static final String REQUEST_DRIVER_LOCATION_MESSAGE = "request.driver.location.message";

    public static final String UNAVAILABLE_ORDER_MESSAGE = "unavailable.order.message";

    public static final String HAS_ALREADY_ORDER_ERROR_MESSAGE = "has.already.order.error.message";

    public static final String ORDER_ID_PART_MESSAGE = "order.id.part.message";

    public static final String NEW_ORDER_BASE_MESSAGE = "new.order.base.message";

    public static final String NEW_ORDER_MESSAGE = "new.order.message";

    public static final String NEW_ORDER_FREE_RIDE_MESSAGE = "new.order.free.ride.message";

    public static final String CAR_MODEL_REQUEST_MESSAGE = "car.model.request.message";

    public static final String LOCATION_SENT_TO_PASSENGER_MESSAGE = "location.sent.to.passenger.message";

    public static final String PASSENGER_INFO_BASE_MESSAGE = "passenger.info.base.message";

    public static final String PASSENGER_INFO_PHONE_NUMBER_MESSAGE = "passenger.info.phone.number.message";

    public static final String PASSENGER_INFO_RATING_MESSAGE = "passenger.info.rating.message";

    public static final String PASSENGER_INFO_REVIEWS_AMOUNT_MESSAGE = "passenger.info.reviews.amount.message";

    public static final String PASSENGER_INFO_MISSING_CONTACTS_MESSAGE = "passenger.info.missing.contacts.message";

    public static final String FURTHER_STEPS_FOR_DRIVER_POSTPONED_MESSAGE = "further.steps.for.driver.postponed.message";

    public static final String FUTURE_ARRIVAL_REMINDER_MESSAGE = "future.arrival.reminder.message";

    public static final String CANCEL_TRIP_BY_PASSENGER_MESSAGE = "cancel.trip.by.passenger.message";

    public static final String STOP_SHIFT_MESSAGE = "stop.shift.message";

    public static final String START_SHIFT_MESSAGE = "start.shift.message";

    public static final String CAR_COLOR_REQUEST_MESSAGE = "car.color.request.message";

    public static final String CAR_PLATE_NUMBER_REQUEST_MESSAGE = "car.plate.number.request.message";

    public static final String DRIVER_LOCATION_REQUEST_MESSAGE = "driver.location.request.message";

    public static final String CAR_INFO_REGISTRATION_FINISHED_MESSAGE = "car.info.registration.finished.message";

    public static final String DESTINATION_POINT_DRIVER_MESSAGE = "destination.point.driver.message";

    public static final String FUTURE_FINISH_ORDER_REMINDER_MESSAGE = "future.finish.order.reminder.message";

    public static final String FINISHED_ORDER_DRIVER_MESSAGE = "finished.order.driver.message";

    public static final String CANCEL_TRIP_BY_DRIVER_SELF_MESSAGE = "cancel.trip.by.driver.self.message";

    public static final String DECLINED_POSTPONED_TRIP_FOR_DRIVER_MESSAGE = "declined.postponed.trip.for.driver.message";

    public static final String CONFIRMED_POSTPONED_TRIP_FOR_DRIVER_MESSAGE = "confirmed.postponed.trip.for.driver.message";

    public static final String DEPARTURE_DETAILS_MESSAGE = "departure.details.message";

    public static final String ADD_REVIEW_ON_PASSENGER_FOR_DRIVERS_MESSAGE = "add.review.on.passenger.for.drivers.message";


    //USER MANAGEMENT
    public static final String SHOW_ALL_USER_DATA_MESSAGE = "show.all.user.data.message";

    public static final String EDIT_USER_NAME_MESSAGE = "edit.user.name.message";

    public static final String EDIT_PHONE_NUMBER_MESSAGE = "edit.phone.number.message";

    public static final String REMOVE_PHONE_NUMBER_MESSAGE = "remove.phone.number.message";

    public static final String HIDDEN_PHONE_NUMBER_FALSE_MESSAGE = "hidden.phone.number.false.message";

    public static final String HIDDEN_PHONE_NUMBER_TRUE_MESSAGE = "hidden.phone.number.true.message";

    public static final String SUCCESSFUL_DATA_SAVED_MESSAGE = "successful.data.saved.message";

    public static final String ERROR_NO_PHONE_NUMBER_MESSAGE = "error.no.phone.number.message";

    public static final String ERROR_DRIVER_NEED_EXIT_SETTINGS_MENU_MESSAGE = "error.driver.need.exit.settings.menu.message";

    public static final String GET_SUPPORT_MESSAGE = "get.support.message";

}
