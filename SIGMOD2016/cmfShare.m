set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 6]);

x=[1, 2, 3, 4, 5];
y1 = [0.138781054	0.208665607	0.22929345	0.240409876	0.250853336];
y2 = [0.443976144	0.553699599	0.57927555	0.590557569	0.597237463];
y3 = [0.464769523	0.553699599	0.57927555	0.590557569	0.597237463];
y4 = [0.219076454	0.354696824	0.407158523	0.434188868	0.444878759];

p1= plot(x, y1, '-k^');
hold on;
p2 = plot(x, y2, '-kv');
hold on;
p3 = plot(x, y3, '-ks');
hold on;
p4 = plot(x, y4, '-kd');

xlabel('the nubmer of shared keywords');
ylabel('CMF');

axis([0.5 5.5 0.0 1.0]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'1','2','3','4','5'});
%leg=legend('Flickr','DBLP','Tencent','DBpedia', 1);
%set(leg,'edgecolor','white');

set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'FontSize', 12);
leg1 = legend([p1, p2],'Flickr','DBLP');
set(leg1,'edgecolor','white');

set(gca, 'LineWidth', 1.5);
ah=axes('position',get(gca,'position'),'visible','off');
set(gca, 'FontSize', 12);
leg2 = legend(ah,[p3, p4],'Tencent','DBpedia');
set(leg2,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);
%applyhatch(gcf,'/\x') ;
