set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [9863.672988	9581.646215	9593.479703	9591.493856	9587.78634];
y2 = [9543.968902	8034.357584	7491.419815	7462.914118	7180.50112];
y3 = [142.8064406	190.3139473	167.3496919	130.6538076	97.40805246];

p1= plot(x, y1, '-kv');
hold on;
p2 = plot(x, y2, '-ks');
hold on;
p3 = plot(x, y3, '-kd');

xlabel('k');
ylabel('time (ms)');

axis([0.5 5.5 0.0 16000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'4','5','6','7','8'});
leg=legend('Global','Local','Dec', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);