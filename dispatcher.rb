require 'bigbertha'
require 'geocoder'
#gem install bigbertha before running
#gem install geocoder before running

Geocoder.configure(
  :units => :km,
  :distances => :linear
 )
 
url = "https://intense-torch-3362.firebaseio.com"
ref = Bigbertha::Ref.new(url)
offerref = ref.child(:Offers)
requestsref = ref.child(:Requests)
running = true

while running
  requests = requestsref.val
  offers = offerref.val
  requests.each_pair do |k,v|
   if v["done"] != "Yes"
   requestDest = v["destination"]
   reqStart = v["start"]
   requestDest2 = requestDest.gsub("lat/lng: (" , '').gsub(")",'').split(",")
   requestDest3 = requestDest2.collect{|i| i.to_f}
      offers.each_pair do |k2,v2|
        offerDest = v2["destination"]
        offerStart = v2["start"]
        offerDest2 = offerDest.gsub("lat/lng: (" , '').gsub(")",'').split(",")
        offerDest3 = offerDest2.collect{|i| i.to_f}
        if (Geocoder::Calculations.distance_between(requestDest3,offerDest3) <= v["radius"].to_i and Geocoder::Calculations.distance_between(reqStart,requestDest3) > Geocoder::Calculations.distance_between(reqStart,offerStart)) 
        ##if v["destination"] == v2["destination"]
          requestsref.child(k,:MatchedOfferID, k2).set({:offerer => k2, :destination => v["Destination"]})
        end
      end
      requestsref.child(k).update(done:"Yes")
   else
      if requestsref.child(k).val["AskToJoin"] != nil
        requestsref.child(k,:AskToJoin).val.each_pair do |k3,v3|
          offerref.child(v3,:Requests).child(k).set({:requester => k, :destination => v["Destination"]})
          requestsref.child(k, :AskToJoin, k3).remove
        end
      end
   end
  end
  #check for fares to be paid
  offers.each_pair do |k,v| 
    if offerref.child(k).val["AskToPay"] != nil
      if v["Passengers"] != nil
        numberOfPeople = v["Passengers"].count + 1;
      else
        numberOfPeople = 1;
      end
      offerDest = v["destination"]
      offerStart = v["start"]
      offerDest2 = offerDest.gsub("lat/lng: (" , '').gsub(")",'').split(",")
      offerDest3 = offerDest2.collect{|i| i.to_f}
      distance = Geocoder::Calculations.distance_between(offerStart,offerDest3)
      price = distance * 5
      offerref.child(k).child(:pricetopay).update(:pricetopay => price / numberOfPeople)
      offerref.child(k).child("AskToPay").remove
    end
  end
  sleep 5
end
